package com.sherry.im;

import android.content.Context;

import com.sherry.im.bean.UserInfo;
import com.sherry.im.dao.UserAccountDao;
import com.sherry.im.dao.UserAccountTable;
import com.sherry.im.db.DBManager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:Sherry
 * Date:2018/10/12
 * 作用：数据模型全局类
 */

public class Model{

    private Context mContext;
    private ExecutorService executorService = Executors.newCachedThreadPool();//线程池
    private DBManager dbManager;

    //创建构造
    private static Model model = new Model();

    private UserAccountDao userAccountDao;

    //私有化构造
    private Model(){
    }

    //获取单例对象
    public static Model getInstance(){
        return model;
    }

    //初始化的方法
    public void init(Context context){
        mContext = context;

        userAccountDao = new UserAccountDao(mContext);

        EventListener eventListener = new EventListener(mContext);
    }

    //获取全局线程池对象
    public ExecutorService getGlobalThreadPool(){
        return executorService;
    }

    //登录成功的处理
    public void loginSuccess(UserInfo account){
        if(account == null) {
            return;
        }

        //若该管理表已存在，则先执行关闭操作
        if(dbManager != null) {
            dbManager.close();
        }

        dbManager = new DBManager(mContext, account.getName());
    }

    //获取管理者的公共方法
    public DBManager getDBManager(){
        return dbManager;
    }

    //获取用户账号数据库的操作类对象
    public UserAccountDao getUserAccountDao(){
        return userAccountDao;
    }
}
