package com.example.shuowang.dragonmate;

/**
 * Created by shuowang on 7/29/15.
 */
import cn.bmob.v3.BmobUser;
public class MyUser extends BmobUser{
    private boolean sex;//true represent male,flase represent female

    public boolean itsSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
