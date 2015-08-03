package com.example.shuowang.dragonmate;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shuowang on 7/31/15.
 */
public class QuestionnaireFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_questionnaire,container,false);

        //这个就是需要写的Fragment,对应的是fragment_questionnaire.xml布局，基本要写的东西参考一下accountSettingFragment就可以了，就是那个bmobUpdate方法。
        //需要添加属性的话在MyUser里添加就可以了。记得写get和set方法。







        return root;
    }
}
