package com.example.shuowang.dragonmate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmob.BmobProFile;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by shuowang on 7/31/15.
 */
public class CustomList extends ArrayAdapter<String>{
    private final Activity context;
    List<String> trueName = new ArrayList<String>();
    List<String> userInfo = new ArrayList<String>();
    List<String> avatar_url = new ArrayList<String>();
    public CustomList(Activity context,
                      List<String>trueName,List<String>userInfo,List<String> avatar_url) {
        super(context, R.layout.list_single, trueName);
        this.context = context;
        this.trueName = trueName;
        this.userInfo = userInfo;
        this.avatar_url = avatar_url;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View root= inflater.inflate(R.layout.list_single, null, true);
        TextView tv_trueName = (TextView) root.findViewById(R.id.TrueNameText);
        TextView tv_userInfo = (TextView) root.findViewById(R.id.UserInfoText);
        ImageView iv_avatar = (ImageView) root.findViewById(R.id.avatar);
        tv_trueName.setText(trueName.get(position));
        tv_userInfo.setText(userInfo.get(position));
        UrlImageViewHelper.setUrlDrawable(iv_avatar,avatar_url.get(position));

        return root;
    }
}

