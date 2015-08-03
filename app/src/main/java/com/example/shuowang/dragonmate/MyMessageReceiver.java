package com.example.shuowang.dragonmate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConstant;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.inteface.OnReceiveListener;
import cn.bmob.im.util.BmobJsonUtil;
import cn.bmob.im.util.BmobLog;
import de.greenrobot.event.EventBus;

/**
 * Created by shuowang on 8/3/15.
 */
public class MyMessageReceiver extends BroadcastReceiver {

    // 事件监听
    //public static ArrayList<EventListener> ehList = new ArrayList<EventListener>();

    @Override
    public void onReceive(Context context, Intent intent) {
    }
}