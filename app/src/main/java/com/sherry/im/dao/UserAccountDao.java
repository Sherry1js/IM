package com.sherry.im.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sherry.im.bean.UserInfo;
import com.sherry.im.db.UserAccountDB;

import static com.sherry.im.dao.UserAccountTable.COL_HXID;

/**
 * Author:Sherry
 * Date:2018/10/12
 * 作用：用户账号数据的操作类，实现增删改查
 */

public class UserAccountDao {

    private final UserAccountDB mHelper;

    public UserAccountDao(Context context){
        mHelper = new UserAccountDB(context);
    }

    //添加用户账号数据到数据库
    public void addAccount(UserInfo user){

        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行添加操作
        ContentValues values = new ContentValues();
        values.put(COL_HXID,user.getHxid());
        values.put(UserAccountTable.COL_NAME,user.getName());
        values.put(UserAccountTable.COL_NICK,user.getNick());
        values.put(UserAccountTable.COL_IMAGE,user.getImage());

        /**代替insert语句执行插入*/
        db.replace(UserAccountTable.TAB_NAME,null,values);
    }

    //根据环信ID获取所有用户信息
    public UserInfo getAccountByHxId(String hxId){

        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        /**执行查询语句*/
        String sql = "select * from " + UserAccountTable.TAB_NAME + " where " + COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});

        UserInfo userInfo = null;
        if(cursor.moveToNext()){
            userInfo = new UserInfo();
            //封装对象
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK)));
            userInfo.setImage(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_IMAGE)));
        }
        /**关闭资源*/
        cursor.close();
        /**返回对象*/
        return userInfo;
    }
}
