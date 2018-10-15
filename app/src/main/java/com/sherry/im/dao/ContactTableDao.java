package com.sherry.im.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sherry.im.bean.UserInfo;
import com.sherry.im.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

import static com.xiaomi.push.service.y.n;
import static com.xiaomi.push.service.y.r;
import static com.xiaomi.push.service.y.u;

/**
 * Author:Sherry
 * Date:2018/10/15
 * 作用：联系人表的操作类
 */

public class ContactTableDao {
    private DBHelper mHelper;

    public ContactTableDao (DBHelper helper) {
        mHelper = helper;
    }

    /**获取所有联系人*/
    public List<UserInfo> getContacts() {
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);

        List<UserInfo> users = new ArrayList<>();
        while(cursor.moveToNext()) {
            UserInfo userInfo = new UserInfo();

            //封装对象
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setImage(cursor.getString(cursor.getColumnIndex(ContactTable.COL_IMAGE)));

            //添加对象到集合中
            users.add(userInfo);
        }
        //关闭资源
        cursor.close();
        //返回数据
        return users;
    }

    /**根据环信ID获取单个联系人信息*/
    public UserInfo getContactByHxId(String hxId) {
        //判断ID是否为空
        if(hxId == null) {
            return null;
        }
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});

        UserInfo userInfo = null;
        if(cursor.moveToNext()) {
            userInfo = new UserInfo();

            //封装对象
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setImage(cursor.getString(cursor.getColumnIndex(ContactTable.COL_IMAGE)));
        }

        cursor.close();
        return userInfo;
    }

    /**根据环信ID获取用户联系人信息*/
    public List<UserInfo> getContactsByHxId(List<String> hxIds) {
        if(hxIds == null || hxIds.size() <= 0){
            return null;
        }

        //通过遍历单个联系人中的hxId来实现用户联系人信息的查找
        List<UserInfo> contacts = new ArrayList<>();
        for(String hxId : hxIds) {
            UserInfo contactByHxId = getContactByHxId(hxId);
            contacts.add(contactByHxId);
        }
        return contacts;
    }

    /**保存单个联系人信息*/
    public void saveContact(UserInfo user,boolean isMyContact) {
        if(user == null){
            return;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行保存语句
        ContentValues values = new ContentValues();
        values.put(ContactTable.COL_HXID,user.getHxid());
        values.put(ContactTable.COL_NAME,user.getName());
        values.put(ContactTable.COL_NICK,user.getNick());
        values.put(ContactTable.COL_IMAGE,user.getImage());
        values.put(ContactTable.COL_IS_CONTACT,isMyContact ? 1 : 0);

        db.replace(ContactTable.TAB_NAME,null,values);
    }

    /**保存联系人信息*/
    public void saveContacts(List<UserInfo> contacts, boolean isMyContact) {
        if(contacts == null || contacts.size() <= 0) {
            return;
        }
        for(UserInfo contact : contacts) {
            saveContact(contact,isMyContact);
        }
    }

    /**删除联系人*/
    public void deleteContactByHxId(String hxId) {
        if(hxId == null) {
            return;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(ContactTable.TAB_NAME,ContactTable.COL_HXID + "=?",new String[]{hxId});
    }
}
