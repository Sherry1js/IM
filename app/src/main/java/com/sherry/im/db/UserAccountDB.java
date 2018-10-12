package com.sherry.im.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sherry.im.dao.UserAccountTable;

import static android.R.attr.version;
import static com.baidu.location.d.j.v;

/**
 * Author:Sherry
 * Date:2018/10/12
 * 作用：用户账号的数据库
 */

public class UserAccountDB extends SQLiteOpenHelper{

    //构造方法
    public UserAccountDB(Context context){
        super(context, "account.db", null, 1);
    }

    //数据库创建的时候调用
    @Override
    public void onCreate(SQLiteDatabase db){
        //创建数据库表的语句
        db.execSQL(UserAccountTable.CREATE_TAB);
    }

    //数据库更新的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){

    }
}
