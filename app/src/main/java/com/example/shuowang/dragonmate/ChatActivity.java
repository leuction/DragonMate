package com.example.shuowang.dragonmate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class ChatActivity extends Activity {


    private Button sendButton = null;
    private EditText contentEditText = null;
    private ListView chatListView = null;
    private List<ChatEntity> chatList = null;
    private ChatAdapter chatAdapter = null;
    private String myObjectId,otherObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            myObjectId = (String) bundle.get("myObjectId");
            otherObjectId = (String) bundle.get("otherObjectId");
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
                chatEntity.setContent(myObjectId);
                chatEntity.setChatTime("2012-09-20 15:12:32");
            }else {
                chatEntity.setComeMsg(true);
                chatEntity.setContent(otherObjectId);
                chatEntity.setChatTime("2012-09-20 15:13:32");
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

    private void send(){
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setChatTime("2012-09-20 15:16:34");
        chatEntity.setContent(contentEditText.getText().toString());
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

            return convertView;
        }

        private class ChatHolder{
            private TextView timeTextView;
            private ImageView userImageView;
            private TextView contentTextView;
        }
    }

}
