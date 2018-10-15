package com.sherry.im.dao;

/**
 * Author:Sherry
 * Date:2018/10/15
 * 作用：创建联系人信息表
 */

public class ContactTable{

    public static final String TAB_NAME = "tab_contact";

    public static final String COL_NAME = "name";
    public static final String COL_HXID = "hxid";
    public static final String COL_NICK = "nick";
    public static final String COL_IMAGE = "image";

    public static final String COL_IS_CONTACT = "is_contact"; //是否为已添加的联系人

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_HXID + " text primary key,"
            + COL_NAME + " text,"
            + COL_NICK + " text,"
            + COL_IMAGE + " text,"
            + COL_IS_CONTACT + " integer);"; //0表示非联系人，1表示联系人
}
