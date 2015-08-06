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
    private String Name;
    private String Age;
    private String Height;
    private String Weight;
    private String Hometown;
    private String Liveplace;
    private String Hobby;
    private String Specialty;
    private String Requirement;
    private int education;
    private int salary;
    private int house;
    private int car;
    private int marriage;


    public String getName() {
        return Name;
    }
    public String getAge() {
        return Age;
    }
    public String getHeight() {
        return Height;
    }
    public String getWeight() {
        return Weight;
    }
    public String getHometown() {
        return Hometown;
    }
    public String getLiveplace() {
        return Liveplace;
    }
    public String getHobby() {
        return Hobby;
    }
    public String getSpecialty() {
        return Specialty;
    }
    public String getRequirement() {
        return Requirement;
    }


    public void setName(String name) {
        Name = name;
    }
    public void setAge(String age) {
        Age = age;
    }
    public void setHeight(String height) {
        Height = height;
    }
    public void setWeight(String weight) {
        Weight = weight;
    }
    public void setHometown(String hometown) {
        Hometown = hometown;
    }
    public void setLiveplace(String liveplace) {
        Liveplace = liveplace;
    }
    public void setHobby(String hobby) {
        Hobby = hobby;
    }
    public void setSpecialty(String specialty) {
        Specialty = specialty;
    }
    public void setRequirement(String requirement) {
        Requirement = requirement;
    }



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

    public int itsEducation() {return education;}
    public void setEducation(int education) { this.education = education; }

    public int itsSalary() {return salary;}
    public void setSalary(int salary) { this.salary = salary; }

    public int itsHouse() {return house;}
    public void setHouse(int house) { this.house = house; }

    public int itsCar() {return car;}
    public void setCar(int car) { this.car = car; }

    public int itsMarriage() {return marriage;}
    public void setMarriage(int marriage) { this.marriage = marriage; }


    public boolean isAvatarInit() {
        return avatarInit;
    }
}
