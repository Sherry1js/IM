package com.sherry.im.db;

import android.content.Context;

import com.sherry.im.dao.ContactTable;
import com.sherry.im.dao.ContactTableDao;
import com.sherry.im.dao.InviteTableDao;

/**
 * Author:Sherry
 * Date:2018/10/15
 * 作用：负责联系表人与邀请表操作类的管理
 */

public class DBManager {

    private final DBHelper dbHelper;
    private ContactTableDao contactTableDao;
    private InviteTableDao inviteTableDao;

    public DBManager(Context context,String name){

        //创建数据库
        dbHelper = new DBHelper(context, name);

        //创建该数据库中两张表的操作类
        contactTableDao = new ContactTableDao(dbHelper);
        inviteTableDao = new InviteTableDao(dbHelper);
    }

    //获取联系人表的操作类对象
    public ContactTableDao getContactTableDao(){
        return contactTableDao;
    }

    //获取邀请信息表的操作类对象
    public InviteTableDao getInviteTableDao(){
        return inviteTableDao;
    }

    //关闭数据库的方法
    public void close(){
        dbHelper.close();
    }
}
