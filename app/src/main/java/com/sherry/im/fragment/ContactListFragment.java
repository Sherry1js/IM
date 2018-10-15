package com.sherry.im.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.sherry.im.R;
import com.sherry.im.activity.AddContactActivity;

/**
 * Author:Sherry
 * Date:2018/10/13
 * 作用：通讯录Fragment
 */

public class ContactListFragment extends EaseContactListFragment{

    @Override
    protected void initView(){
        super.initView();

        titleBar.setRightImageResource(R.drawable.em_add);

        //添加头布局
        View headerView = View.inflate(getActivity(), R.layout.header_list_fragment, null);
        listView.addHeaderView(headerView);
    }

    @Override
    protected void setUpView(){
        super.setUpView();

        //添加titleBar的点击事件实现好友添加
        titleBar.setRightLayoutClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(),AddContactActivity.class);
                startActivity(intent);
            }
        });
    }
}
