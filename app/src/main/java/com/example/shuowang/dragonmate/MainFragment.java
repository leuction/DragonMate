package com.example.shuowang.dragonmate;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by shuowang on 7/31/15.
 */
public class MainFragment extends Fragment {
    ListView showList;
    List<String> objectId = new ArrayList<String>();
    List<String> trueName = new ArrayList<String>();
    List<String> userInfo = new ArrayList<String>();
    List<String> avatar_url = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_main,container,false);

        final MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        BmobQuery<MyUser> selfQuery = new BmobQuery<MyUser>();
        selfQuery.addWhereEqualTo("objectId", currentUser.getObjectId());
        selfQuery.findObjects(getActivity(), new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> list) {
                boolean currentUserSex = list.get(0).itsSex();
                BmobQuery<MyUser> otherQuery = new BmobQuery<MyUser>();
                otherQuery.addWhereEqualTo("sex", !currentUserSex);
                otherQuery.findObjects(getActivity(), new FindListener<MyUser>() {
                    @Override
                    public void onSuccess(List<MyUser> list) {
                        Toast.makeText(getActivity(), "列表初始化成功", Toast.LENGTH_SHORT).show();

                        for (MyUser myUser : list) {
                            objectId.add(myUser.getObjectId().toString());
                            if (myUser.getName() != null) {
                                trueName.add("姓名："+myUser.getName().toString());
                            } else {
                                trueName.add("用户名：" + myUser.getUsername().toString());
                            }
                            if (myUser.getAge() != null) {
                                userInfo.add("年龄："+myUser.getAge().toString());
                            } else {
                                userInfo.add("年龄："+"未填写");
                            }
                            if (myUser.isAvatarInit()) {
                                avatar_url.add(myUser.getMyAvatar().getFileUrl(getActivity()));
                            } else {
                                avatar_url.add("http://file.bmob.cn/M01/AB/44/oYYBAFW8UkWATmoPAACguaHH6So482.jpg");
                            }
                        }
                        CustomList adapter = new CustomList(getActivity(), trueName, userInfo, avatar_url);
                        showList = (ListView) root.findViewById(R.id.list);
                        showList.setAdapter(adapter);

                        showList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, DetailFragment.newInstance(objectId.get(position).toString())).commit();
                                trueName.clear();
                                userInfo.clear();
                                avatar_url.clear();
                                objectId.clear();
                            }
                        });

                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(getActivity(), "无法连接到服务器，请检查您的网络设置.", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(getActivity(), "无法连接到服务器，请检查您的网络设置.", Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }


}
