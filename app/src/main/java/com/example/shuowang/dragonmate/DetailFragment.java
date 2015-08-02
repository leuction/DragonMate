package com.example.shuowang.dragonmate;


import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by shuowang on 7/31/15.
 */
public class DetailFragment extends Fragment{
    ListView infoList;
    ImageView iv_avatar;
    List<String> items = null;
    String objectId,userName,phone,e_mail;
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
                    UrlImageViewHelper.setUrlDrawable(iv_avatar,myUser.getMyAvatar().getFileUrl(getActivity()));
                } else {
                    UrlImageViewHelper.setUrlDrawable(iv_avatar,"http://file.bmob.cn/M01/AB/44/oYYBAFW8UkWATmoPAACguaHH6So482.jpg");
                }
                userName =myUser.getUsername().toString();
                phone = myUser.getMobilePhoneNumber().toString();
                e_mail = myUser.getEmail().toString();
                items.add("用户名：" + userName);
                items.add("E-mail:" + e_mail);
                items.add("电话:" + phone);
                items.add("性别：" + sexTOString(myUser.itsSex()));
                items.add("发送消息");
                items.add("插入联系人");
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
        infoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position ==1){
                    Intent data=new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:"+e_mail));
                    data.putExtra(Intent.EXTRA_TEXT, "你好，我是来自XXX的" + BmobUser.getCurrentUser(getActivity()).getUsername().toString());
                    data.putExtra(Intent.EXTRA_SUBJECT, "交友邮件");
                    startActivity(data);
                }
                if(position ==2){
                    Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
                if(position==4){
                    Intent intent = new Intent(getActivity(),ChatActivity.class);
                    startActivity(intent);
                }
                if(position==5){
                    ContactInsert(userName,phone,e_mail);
                    Toast.makeText(getActivity(),"插入成功",Toast.LENGTH_SHORT).show();
                }
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
    public void ContactInsert(String CI_username,String CI_phone,String CI_email) {
        ContentValues values = new ContentValues();
        /*
         * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId
         */
        Uri rawContactUri = getActivity().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);

        //往data表里写入姓名数据
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE); //内容类型
        values.put(StructuredName.GIVEN_NAME, CI_username);
        getActivity().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);

        //往data表里写入电话数据
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, CI_phone);
        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
        getActivity().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);

        //往data表里写入Email的数据
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
        values.put(Email.DATA, CI_email);
        values.put(Email.TYPE, Email.TYPE_WORK);
        getActivity().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);

    }
}
