package com.sherry.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.sherry.im.Model;
import com.sherry.im.R;
import com.sherry.im.bean.UserInfo;

import static android.os.Message.obtain;

public class SplashActivity extends Activity{

    private static final int DELAYED_TIME = 2000;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            //判断当前活动是否已退出，若是，则不处理handler中的消息
            if(isFinishing()) {
                return;
            }
            //判断进入主页面还是登录页面
            toMainOrLogin();
        }
    };

    private void toMainOrLogin(){
        //请求服务器的操作为耗时操作，需开子线程
        //设置全局线程池，优化内存
        Model.getInstance().getGlobalThreadPool().execute(new Runnable(){
            @Override
            public void run(){
                //判断当前账号是否已登录过
                if(EMClient.getInstance().isLoggedInBefore()){
                    //获取当前登录的用户信息
                    UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId
                            (EMClient.getInstance().getCurrentUser());
                    if(account == null) {
                        //未登录，跳转到登录页面
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                    } else {
                        //登录成功后执行模型层的登录后处理方法
                        Model.getInstance().loginSuccess(account);

                        //已登录，跳转到主页面
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    //未登录，跳转到登录页面
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //延迟跳转页面
        handler.sendMessageDelayed(Message.obtain(), DELAYED_TIME);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
