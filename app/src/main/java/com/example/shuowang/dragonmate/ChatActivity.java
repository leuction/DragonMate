package com.example.shuowang.dragonmate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.utils.BmobLog;
import com.example.shuowang.dragonmate.util.TimeUtil;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.config.BmobConstant;
import cn.bmob.im.util.BmobJsonUtil;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import de.greenrobot.event.EventBus;


public class ChatActivity extends BaseActivity {


    private Button sendButton = null;
    private EditText contentEditText = null;
    private ListView chatListView = null;
    private List<ChatEntity> chatList = null;
    private ChatAdapter chatAdapter = null;
    private String selfObjectId,targetObjectId;
    private String selfUserAvatarUrl,targetUserAvatarUrl;
    private String time = TimeUtil.getCurrentTime(TimeUtil.FORMAT_MONTH_DAY_TIME);
    MyMessageReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);


        //注册广播接收器
        initNewMessageBroadCast();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            selfObjectId = (String) bundle.get("selfObjectId");
            targetObjectId = (String) bundle.get("targetObjectId");
            selfUserAvatarUrl = (String) bundle.get("selfUserAvatarUrl");
            targetUserAvatarUrl = (String) bundle.get("targetUserAvatarUrl");
        }
        contentEditText = (EditText) this.findViewById(R.id.C_et_content);
        sendButton = (Button) this.findViewById(R.id.C_btn_send);
        chatListView = (ListView) this.findViewById(R.id.C_listview);


        chatList = new ArrayList<ChatEntity>();
        ChatEntity chatEntity = null;
        for (int i = 0; i < 2; i++) {
            chatEntity = new ChatEntity();
            if (i % 2 == 0) {
                chatEntity.setComeMsg(false);
                chatEntity.setContent("123");
                chatEntity.setChatTime(time);
                chatEntity.setUserImage(selfUserAvatarUrl);
            }else {
                chatEntity.setComeMsg(true);
                chatEntity.setContent("123");
                chatEntity.setChatTime(time);
                chatEntity.setUserImage(targetUserAvatarUrl);
            }
            chatList.add(chatEntity);
        }

        chatAdapter = new ChatAdapter(this,chatList);
        chatListView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!contentEditText.getText().toString().equals("")) {
                    //发送消息
                    send();
                } else {
                    Toast.makeText(ChatActivity.this, "Content is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        chatListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(findViewById(R.id.C_et_content).getWindowToken(), 0); //强制隐藏键盘
                }
                return false;
            }
        });
        contentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatListView.setSelection(chatListView.getBottom());
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });
    }


    private void initNewMessageBroadCast() {

        // 注册接收消息广播
        receiver = new MyMessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.bmob.push.action.MESSAGE");
        //设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
        intentFilter.setPriority(5);
        ChatActivity.this.registerReceiver(receiver, intentFilter);
        toast("注册成功");
    }

    private void send(){

        BmobChatUser targetUser = new BmobChatUser();
        targetUser.setObjectId(targetObjectId);
        // 组装BmobMessage对象
        BmobMsg message = BmobMsg.createTextSendMsg(this, targetObjectId, contentEditText.getText().toString());
        //不带监听回调，默认发送完成，将数据保存到本地消息表和最近会话表中
        manager.sendTextMessage(targetUser, message);
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setChatTime(time);
        chatEntity.setContent(contentEditText.getText().toString());
        chatEntity.setUserImage(selfUserAvatarUrl);
        chatEntity.setComeMsg(false);
        chatList.add(chatEntity);
        chatAdapter.notifyDataSetChanged();
        chatListView.setSelection(chatList.size() - 1);
        contentEditText.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class MyMessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            String json = intent.getStringExtra("msg");
            try {
                JSONObject jo = new JSONObject(json);
                String toId = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_TOID);
                String fromID = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_TARGETID);
                String msg = BmobJsonUtil.getString(jo,BmobConstant.PUSH_KEY_CONTENT);


                if(toId.equals(selfObjectId)&&fromID.equals(targetObjectId)){

                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setChatTime(time);
                    chatEntity.setContent(msg);
                    chatEntity.setUserImage(targetUserAvatarUrl);
                    chatEntity.setComeMsg(true);
                    chatList.add(chatEntity);
                    chatAdapter.notifyDataSetChanged();
                    chatListView.setSelection(chatList.size() - 1);
                }


            } catch (JSONException e) {
                BmobLog.i("parseMessage错误：" + e.getMessage());
            }
            // 记得把广播给终结掉
            //abortBroadcast();


            BmobLog.i("收到的message = " + json);
        }
    }




    private class ChatAdapter extends BaseAdapter {

        private Context context = null;
        private List<ChatEntity> chatList = null;
        private LayoutInflater inflater = null;
        private int COME_MSG = 0;
        private int TO_MSG = 1;

        public ChatAdapter(Context context,List<ChatEntity> chatList){
            this.context = context;
            this.chatList = chatList;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return chatList.size();
        }

        @Override
        public Object getItem(int position) {
            return chatList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            // 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
            ChatEntity entity = chatList.get(position);
            if (entity.isComeMsg())
            {
                return COME_MSG;
            }else{
                return TO_MSG;
            }
        }

        @Override
        public int getViewTypeCount() {
            // 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatHolder chatHolder = null;
            if (convertView == null) {
                chatHolder = new ChatHolder();
                if (chatList.get(position).isComeMsg()) {
                    convertView = inflater.inflate(R.layout.receive_chat_item, null);
                }else {
                    convertView = inflater.inflate(R.layout.send_chat_item, null);
                }
                chatHolder.timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
                chatHolder.contentTextView = (TextView) convertView.findViewById(R.id.tv_content);
                chatHolder.userImageView = (ImageView) convertView.findViewById(R.id.userImage);
                convertView.setTag(chatHolder);
            }else {
                chatHolder = (ChatHolder)convertView.getTag();
            }

            chatHolder.timeTextView.setText(chatList.get(position).getChatTime());
            chatHolder.contentTextView.setText(chatList.get(position).getContent());
            //chatHolder.userImageView.setImageResource(chatList.get(position).getUserImage());
            UrlImageViewHelper.setUrlDrawable(chatHolder.userImageView,chatList.get(position).getUserImage());

            return convertView;
        }

        private class ChatHolder{
            private TextView timeTextView;
            private ImageView userImageView;
            private TextView contentTextView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
