package com.example.shuowang.dragonmate;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.Bmob;
import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;

/**
 * Created by shuowang on 7/30/15.
 */
public class BaseActivity extends Activity{
    BmobUserManager userManager;
    BmobChatManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // 初始化BmobSDK
        Bmob.initialize(this, "d594cbc8449e49a5be5d1b752db1722c");

        //可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试，正式发布应注释此句。
        BmobChat.DEBUG_MODE = true;
        //BmobIM SDK初始化--只需要这一段代码即可完成初始化
        BmobChat.getInstance(this).init("d594cbc8449e49a5be5d1b752db1722c");

        userManager = BmobUserManager.getInstance(this);
        manager = BmobChatManager.getInstance(this);
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
