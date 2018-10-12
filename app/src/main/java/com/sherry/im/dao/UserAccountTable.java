package com.sherry.im.dao;

/**
 * Author:Sherry
 * Date:2018/10/12
 * 作用：创建数据库表
 */

public class UserAccountTable {

    public static final String TAB_NAME = "tab_account";
    public static final String COL_NAME = "name";
    public static final String COL_HXID = "hxid";
    public static final String COL_NICK = "nick";
    public static final String COL_IMAGE = "image";

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_NAME + " text primary key,"
            + COL_HXID + " text,"
            + COL_NICK + " text,"
            + COL_IMAGE + "text);";
}
