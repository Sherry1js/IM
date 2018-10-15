package com.sherry.im;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.sherry.im.bean.InvitationInfo;
import com.sherry.im.bean.UserInfo;
import com.sherry.im.utils.Constant;
import com.sherry.im.utils.SpUtils;

/**
 * Author:Sherry
 * Date:2018/10/15
 * 作用：全局事件监听类
 */

public class EventListener{

    private Context mContext;
    private final LocalBroadcastManager mLBM;

    public EventListener(Context context){
        mContext = context;

        mLBM = LocalBroadcastManager.getInstance(mContext);
        //注册联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
    }

    private final EMContactListener emContactListener = new EMContactListener(){

        //联系人添加后执行的方法
        @Override
        public void onContactAdded(String hxid){
            //数据库更新
            Model.getInstance().getDBManager().getContactTableDao().saveContact(new UserInfo(hxid), true);
            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //联系人删除后执行的方法
        @Override
        public void onContactDeleted(String hxid){
            //数据库更新
            Model.getInstance().getDBManager().getContactTableDao().deleteContactByHxId(hxid);
            Model.getInstance().getDBManager().getInviteTableDao().removeInvitation(hxid);
            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //接收到联系人新的邀请
        @Override
        public void onContactInvited(String hxid, String reason){
            //数据库更新
            InvitationInfo invitationInfo = new InvitationInfo();

            //封装信息
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);

            Model.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);

            //接收到信息的红点提示
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //对方同意你的好友邀请
        @Override
        public void onContactAgreed(String hxid){
            //数据库更新
            InvitationInfo invitationInfo = new InvitationInfo();

            //封装信息
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);

            Model.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);

            //接收到信息的红点提示
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //对方拒绝你的好友邀请
        @Override
        public void onContactRefused(String s){
            //接收到信息的红点提示
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };
}
