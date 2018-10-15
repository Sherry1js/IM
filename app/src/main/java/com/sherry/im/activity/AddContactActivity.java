package com.sherry.im.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.sherry.im.Model;
import com.sherry.im.R;
import com.sherry.im.bean.UserInfo;

import static com.xiaomi.push.service.y.n;

public class AddContactActivity extends Activity{

    private Button bt_search;
    private EditText edit_name;
    private RelativeLayout rl_search_result;
    private Button bt_back;
    private Button bt_add_friend;
    private TextView tv_friend;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        initView();
        initListener();
    }

    private void initListener(){
        bt_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                find();
            }
        });
        bt_add_friend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                add();
            }
        });
    }

    //查找按钮的逻辑处理
    private void find(){
        final String name = edit_name.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(AddContactActivity.this, "输入的用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //在服务器上查询是否存在该用户
        Model.getInstance().getGlobalThreadPool().execute(new Runnable(){
            @Override
            public void run(){
                userInfo = new UserInfo(name);
                
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        rl_search_result.setVisibility(View.VISIBLE);
                        tv_friend.setText(userInfo.getName());
                    }
                });
            }
        });
    }

    //添加按钮的逻辑处理
    private void add(){
        Model.getInstance().getGlobalThreadPool().execute(new Runnable(){
            @Override
            public void run(){
                try {
                    //在环信服务器上通过管理员实现添加好友
                    EMClient.getInstance().contactManager().addContact(userInfo.getName(),"添加好友");
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            Toast.makeText(AddContactActivity.this, "好友添加邀请发送成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch(HyphenateException e) {
                    e.printStackTrace();
                    Toast.makeText(AddContactActivity.this, "好友添加邀请发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView(){
        bt_search = (Button) findViewById(R.id.bt_search);
        edit_name = (EditText) findViewById(R.id.edit_name);
        rl_search_result = (RelativeLayout) findViewById(R.id.rl_search_result);
        //bt_back = (Button) findViewById(R.id.bt_back);
        bt_add_friend = (Button) findViewById(R.id.bt_add_friend);
        tv_friend = (TextView) findViewById(R.id.tv_friend);
    }
}
