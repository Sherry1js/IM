package com.sherry.im.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.sherry.im.R;
import com.sherry.im.activity.AddContactActivity;
import com.sherry.im.activity.InviteActivity;
import com.sherry.im.utils.Constant;
import com.sherry.im.utils.SpUtils;

/**
 * Author:Sherry
 * Date:2018/10/13
 * 作用：联系人Fragment
 */

public class ContactListFragment extends EaseContactListFragment{

    private ImageView iv_contact_red;
    private LocalBroadcastManager mLBM;
    private LinearLayout ll_invite;
    private LinearLayout ll_group;

    private BroadcastReceiver ContactInviteChangeReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            //更新红点状态
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
        }
    };

    @Override
    protected void initView(){
        super.initView();

        titleBar.setRightImageResource(R.drawable.em_add);

        //添加头布局
        View headerView = View.inflate(getActivity(), R.layout.header_list_fragment, null);
        listView.addHeaderView(headerView);

        //获取红点的实例
        iv_contact_red = (ImageView) headerView.findViewById(R.id.iv_contact_red);

        //获取邀请信息条目的实例
        ll_invite = (LinearLayout) headerView.findViewById(R.id.ll_invite);
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

        //初始化红点的显示
        boolean isNewInvite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE,false);
        iv_contact_red.setVisibility(isNewInvite ? View.VISIBLE : View.GONE);

        //邀请信息条目的点击事件
        ll_invite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //红点消失
                iv_contact_red.setVisibility(View.GONE);
                SpUtils.getInstance().save(Constant.CONTACT_INVITE_CHANGED,false);

                //跳转到邀请信息列表页面
                Intent intent = new Intent(getActivity(),InviteActivity.class);
                startActivity(intent);
            }
        });

        //注册广播
        mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.registerReceiver(ContactInviteChangeReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
    }
}
