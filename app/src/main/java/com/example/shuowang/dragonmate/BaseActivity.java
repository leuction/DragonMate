package com.example.shuowang.dragonmate;

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
        userManager = BmobUserManager.getInstance(this);
        manager = BmobChatManager.getInstance(this);
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
