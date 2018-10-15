package com.sherry.im.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sherry.im.dao.ContactTable;
import com.sherry.im.dao.InviteTable;

import static android.R.attr.version;
import static com.xiaomi.push.service.y.C;

/**
 * Author:Sherry
 * Date:2018/10/15
 * 作用：
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name){
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //创建联系人的表
        db.execSQL(ContactTable.CREATE_TAB);
        //创建邀请信息的表
        db.execSQL(InviteTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){

    }
}
