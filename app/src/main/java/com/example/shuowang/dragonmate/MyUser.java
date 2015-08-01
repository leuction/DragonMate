package com.example.shuowang.dragonmate;

/**
 * Created by shuowang on 7/29/15.
 */
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser{
    private boolean sex;//true represent male,flase represent female
    private boolean avatarInit=false;
    private BmobFile avatar;//头像

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
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
