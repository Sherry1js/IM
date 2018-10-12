package com.sherry.im.bean;

/**
 * Author:Sherry
 * Date:2018/10/12
 * 作用：用户数据bean类
 */

public class UserInfo {

    private String name; //用户名称
    private String hxid; //环信ID
    private String nick; //用户昵称
    private String image; //用户头像

    public UserInfo(){

    }

    public UserInfo(String name){
        this.name = name;
        //this.image = image;
        this.nick = name;
        this.hxid = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getNick(){
        return nick;
    }

    public void setNick(String nick){
        this.nick = nick;
    }

    public String getHxid(){
        return hxid;
    }

    public void setHxid(String hxid){
        this.hxid = hxid;
    }

    @Override
    public String toString(){
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", hxid='" + hxid + '\'' +
                ", nick='" + nick + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
