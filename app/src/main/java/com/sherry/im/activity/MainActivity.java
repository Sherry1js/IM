package com.sherry.im.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

import com.sherry.im.R;
import com.sherry.im.fragment.ChatFragment;
import com.sherry.im.fragment.ContactListFragment;
import com.sherry.im.fragment.SettingFragment;

public class MainActivity extends FragmentActivity{

    private RadioGroup main_bottom;
    private ChatFragment chatFragment;
    private ContactListFragment listFragment;
    private SettingFragment settingFragment;
    private Button bt_chat;
    private Button bt_list;
    private Button bt_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //初始化Fragment
        initData();
        //初始化监听
        initListener();
    }

    private void initListener(){
        main_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            Fragment fragment = null;
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i){
                switch(i){
                    case R.id.bt_chat:
                        fragment = chatFragment;
                        break;

                    case R.id.bt_list:
                        fragment = listFragment;
                        break;

                    case R.id.bt_setting:
                        fragment = settingFragment;
                        break;
                }
                //实现Fragment切换的方法
                switchFragment(fragment);
            }
        });
        //默认选中会话页面
        main_bottom.check(R.id.bt_chat);
    }

    private void switchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }

    private void initData(){
        chatFragment = new ChatFragment();
        listFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
    }

    private void initView(){
        main_bottom = (RadioGroup) findViewById(R.id.main_bottom);
        bt_chat = (Button) findViewById(R.id.bt_chat);
        bt_list = (Button) findViewById(R.id.bt_list);
        bt_setting = (Button) findViewById(R.id.bt_setting);
    }
}
