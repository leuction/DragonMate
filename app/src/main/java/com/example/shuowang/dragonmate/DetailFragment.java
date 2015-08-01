package com.example.shuowang.dragonmate;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by shuowang on 7/31/15.
 */
public class DetailFragment extends Fragment{
    ListView infoList;
    ImageView iv_avatar;
    List<String> items = null;
    String objectId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        iv_avatar = (ImageView) root.findViewById(R.id.D_profile_image);
        infoList = (ListView) root.findViewById(R.id.D_InfoListView);
        items = new ArrayList<String>();
        objectId = getArguments().getString("objectId");
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.getObject(getActivity(), objectId, new GetListener<MyUser>() {
            @Override
            public void onSuccess(MyUser myUser) {
                if (myUser.isAvatarInit()) {
                    UrlImageViewHelper.setUrlDrawable(iv_avatar,myUser.getAvatar().getFileUrl(getActivity()));
                } else {
                    UrlImageViewHelper.setUrlDrawable(iv_avatar,"http://file.bmob.cn/M01/AB/44/oYYBAFW8UkWATmoPAACguaHH6So482.jpg");
                }
                items.add("用户名：" + myUser.getUsername().toString());
                items.add("E-mail:" + myUser.getEmail().toString());
                items.add("电话:" + myUser.getMobilePhoneNumber().toString());
                items.add("性别：" + sexTOString(myUser.itsSex()));
                infoList.setAdapter(new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_activated_1,
                        android.R.id.text1,
                        items));
            }

            @Override
            public void onFailure(int i, String s) {

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
