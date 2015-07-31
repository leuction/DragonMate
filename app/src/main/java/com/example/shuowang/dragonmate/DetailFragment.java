package com.example.shuowang.dragonmate;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by shuowang on 7/31/15.
 */
public class DetailFragment extends Fragment{
    ListView infoList;
    List<String> items = null;
    String objectId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        infoList = (ListView) root.findViewById(R.id.D_InfoListView);
        items = new ArrayList<String>();
        objectId = getArguments().getString("objectId");
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("objectId",objectId);
        query.findObjects(getActivity(), new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> list) {
                items.add("用户名："+list.get(0).getUsername().toString());
                items.add("E-mail:"+list.get(0).getEmail().toString());
                items.add("电话:"+list.get(0).getMobilePhoneNumber().toString());
                items.add("性别："+sexTOString(list.get(0).itsSex()));
                infoList.setAdapter(new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_activated_1,
                        android.R.id.text1,
                        items));
            }

            @Override
            public void onError(int i, String s) {

            }
        });




        return root;
    }

    //传入bundle
    public static DetailFragment newInstance(String objectId){
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("objectId", objectId);
        detailFragment.setArguments(args);
        return detailFragment;
    }
    public static String sexTOString(boolean sex){
        if(sex){
            return "男";
        }else {
            return "女";
        }
    }
}
