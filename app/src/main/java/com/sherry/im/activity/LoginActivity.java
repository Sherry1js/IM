package com.sherry.im.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.sherry.im.Model;
import com.sherry.im.R;
import com.sherry.im.bean.UserInfo;

public class LoginActivity extends AppCompatActivity{

    private EditText et_username;
    private EditText et_password;
    private Button bt_register;
    private Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化控件
        initView();

        //初始化监听事件
        initListener();
    }

    private void initView(){
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_register = (Button) findViewById(R.id.bt_register);
        bt_login = (Button) findViewById(R.id.bt_login);
    }

    private void initListener(){
        //注册按钮的监听事件
        bt_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                register();
            }
        });
        //登录按钮的监听事件
        bt_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                login();
            }
        });
    }

    //登录的业务逻辑处理
    private void login(){

        //获取用户输入的用户名和密码
        final String loginName = et_username.getText().toString();
        final String loginPassword = et_password.getText().toString();

        //判断是否为空
        if(TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPassword)) {
            Toast.makeText(LoginActivity.this, "输入的用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //在服务器上登录
        Model.getInstance().getGlobalThreadPool().execute(new Runnable(){
            @Override
            public void run(){
                EMClient.getInstance().login(loginName, loginPassword, new EMCallBack(){

                    @Override
                    public void onSuccess(){
                        //对模型层数据的处理
                        Model.getInstance().loginSuccess(new UserInfo(loginName));

                        //保存数据到数据库中
                        Model.getInstance().getUserAccountDao().addAccount(new UserInfo(loginName));

                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                //提示登录成功
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                //跳转到主页面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s){

                    }
                });
            }
        });
    }

    //注册的业务逻辑处理
    private void register(){

        //获取用户输入的用户名和密码
        final String registerName = et_username.getText().toString();
        final String registerPassword = et_password.getText().toString();

        //判断是否为空
        if(TextUtils.isEmpty(registerName) || TextUtils.isEmpty(registerPassword)) {
            Toast.makeText(LoginActivity.this, "输入的用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //去服务器注册账号
        Model.getInstance().getGlobalThreadPool().execute(new Runnable(){
            @Override
            public void run(){
                try {
                    //在环信服务器注册账号
                    EMClient.getInstance().createAccount(registerName, registerPassword);

                    //更新页面（子线程中不能直接更新）
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch(HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
