package com.sherry.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.sherry.im.Model;
import com.sherry.im.R;
import com.sherry.im.activity.LoginActivity;

import static com.sherry.im.R.id.bt_logout;

/**
 * Author:Sherry
 * Date:2018/10/13
 * 作用：设置Fragment
 */

public class SettingFragment extends Fragment {
    //public Context mContext;
    private Button bt_logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);

        initView(view);
        return view;
    }

    private void initView(View view){
        bt_logout = (Button) view.findViewById(R.id.bt_logout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    //处理具体的业务逻辑
    private void initData(){
        bt_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Model.getInstance().getGlobalThreadPool().execute(new Runnable(){
                    @Override
                    public void run(){
                        //登录环信服务器退出登录
                        EMClient.getInstance().logout(false, new EMCallBack(){
                            @Override
                            public void onSuccess(){
                                //关闭DBHelper
                                Model.getInstance().getDBManager().close();

                                getActivity().runOnUiThread(new Runnable(){
                                    @Override
                                    public void run(){
                                        //更新ui，提示退出成功
                                        Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
                                        //回到登录页面
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, String s){
                                getActivity().runOnUiThread(new Runnable(){
                                    @Override
                                    public void run(){
                                        Toast.makeText(getActivity(), "退出失败", Toast.LENGTH_SHORT).show();
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
        });
    }
}
