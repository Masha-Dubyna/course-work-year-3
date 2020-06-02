package com.gmail.mashamatrushenko.courceapp;

import java.util.Date;

public class Message {
    private String userName;
    private String text;
    private long time;

    public Message() {}

    public Message(String userName, String text) {
        this.userName = userName;
        this.text = text;
        this.time = new Date().getTime();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
