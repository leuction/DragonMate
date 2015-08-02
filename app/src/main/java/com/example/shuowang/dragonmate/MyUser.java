package com.example.shuowang.dragonmate;

/**
 * Created by shuowang on 7/29/15.
 */
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobChatUser{
    private boolean sex;//true represent male,flase represent female
    private boolean avatarInit=false;
    private BmobFile myAvatar;//头像

    public BmobFile getMyAvatar() {
        return myAvatar;
    }

    public void setMyAvatar(BmobFile myAvatar) {
        this.myAvatar = myAvatar;
        avatarInit = true;
    }

    public boolean itsSex() {
        return sex;
    }

    public void setSex(boolean sex) { this.sex = sex; }

    public boolean isAvatarInit() {
        return avatarInit;
    }
}
