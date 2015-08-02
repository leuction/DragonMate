package com.example.shuowang.dragonmate;

/**
 * Created by shuowang on 8/2/15.
 */
public class ChatEntity {

    private String userImage;
    private String content;
    private String chatTime;
    private boolean isComeMsg;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getChatTime() {
        return chatTime;
    }
    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
    public boolean isComeMsg() {
        return isComeMsg;
    }
    public void setComeMsg(boolean isComeMsg) {
        this.isComeMsg = isComeMsg;
    }

}
