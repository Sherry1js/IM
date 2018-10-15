package com.sherry.im.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.games.multiplayer.Invitation;
import com.sherry.im.bean.GroupInfo;
import com.sherry.im.bean.InvitationInfo;
import com.sherry.im.bean.UserInfo;
import com.sherry.im.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Sherry
 * Date:2018/10/15
 * 作用：创建邀请表的操作类
 */

public class InviteTableDao {
    private DBHelper mHelper;

    public InviteTableDao(DBHelper Helper){
        mHelper = Helper;
    }

    //添加邀请
    public void addInvitation(InvitationInfo invitationInfo) {
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行添加语句
        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_REASON,invitationInfo.getReason());
        values.put(InviteTable.COL_STATUS,invitationInfo.getStatus().ordinal()); //根据枚举的序号添加

        UserInfo user = invitationInfo.getUser();
        if(user != null) {
            values.put(InviteTable.COL_USER_HXID,invitationInfo.getUser().getHxid());
            values.put(InviteTable.COL_USER_NAME,invitationInfo.getUser().getName());
        } else {
            values.put(InviteTable.COL_GROUP_HXID,invitationInfo.getGroup().getGroupId());
            values.put(InviteTable.COL_GROUP_NAME,invitationInfo.getGroup().getGroupName());
            values.put(InviteTable.COL_USER_HXID,invitationInfo.getGroup().getInvitePerson());
        }

        db.replace(InviteTable.TAB_NAME,null,values);
    }

    //获取所有邀请信息
    public List<InvitationInfo> getInvitations() {

        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行查询语句
        String sql = "select * from " + InviteTable.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);

        List<InvitationInfo> invitations = new ArrayList<>();
        while(cursor.moveToNext()) {
            InvitationInfo invitationInfo = new InvitationInfo();

            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable.COL_REASON)));
            invitationInfo.setStatus(int2InviteStatus(cursor.getInt(cursor.getColumnIndex(InviteTable.COL_STATUS))));

            //判断该邀请信息是联系人邀请还是群邀请
            String groupId = cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID));

            //若群组ID为空，则为联系人邀请信息，否则为群邀请
            if(groupId == null) {

                UserInfo userInfo = new UserInfo();
                //对联系人信息进行封装
                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_NAME)));
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_NAME)));

            } else {

                GroupInfo groupInfo = new GroupInfo();
                //对群组信息进行封装
                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_NAME)));
                groupInfo.setInvitePerson(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID)));

            }
            invitations.add(invitationInfo);
        }
        cursor.close();
        return invitations;
    }

    //将int类型状态转换为邀请的状态
    private InvitationInfo.InvitationStatus int2InviteStatus(int intStatus){

        if (intStatus == InvitationInfo.InvitationStatus.NEW_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE;
        }

        return null;
    }

    //删除邀请信息
    public void removeInvitation(String hxId) {
        if(hxId == null){
            return;
        }
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行删除语句
        db.delete(InviteTable.TAB_NAME,InviteTable.COL_USER_HXID + "=?",new String[]{hxId});
    }

    //更新邀请状态
    public void updateInvitationStatus(InvitationInfo.InvitationStatus invitationStatus,String hxId){
        if(hxId == null) {
            return;
        }

        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行更新操作
        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_STATUS,invitationStatus.ordinal());

        db.update(InviteTable.TAB_NAME,values,InviteTable.COL_USER_HXID + "=?",new String[]{hxId});
    }
}
