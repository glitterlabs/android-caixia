package com.glitterlabs.caixia.Adapters;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glitterlabs.caixia.AddFriendAvtivity;
import com.glitterlabs.caixia.Databases.DatabseHelper;
import com.glitterlabs.caixia.Models.Friend;
import com.glitterlabs.caixia.R;

import java.util.ArrayList;


public class SearchFriendAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Friend> mSearchFriendList = new ArrayList<Friend>();
    DatabseHelper mdatabase;

    public SearchFriendAdapter(Context context, ArrayList<Friend> Friend) {
        this.mContext = context;
        this.mSearchFriendList = Friend;
        mdatabase = new DatabseHelper(mContext);
    }

    @Override
    public int getCount() {
        return mSearchFriendList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView tvName, tvUserName;
        ImageView ivAdd;

        convertView = LayoutInflater.from(mContext).inflate(R.layout.search_friend_list_item, null);
        tvName = (TextView) convertView.findViewById(R.id.tv_FriendName);
        final String name = mSearchFriendList.get(position).getFriendName();
        tvName.setText(name);
        tvUserName = (TextView) convertView.findViewById(R.id.tv_userName_searchFriend);
        tvUserName.setText(mSearchFriendList.get(position).getUserName());
        ivAdd = (ImageView) convertView.findViewById(R.id.iv_AddFriend);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFriend(mSearchFriendList.get(position).getFriendName(), mSearchFriendList.get(position).getUserName(), mSearchFriendList.get(position).getUserID());
                // Toast.makeText(mContext,"Added "+name+ " as friend",Toast.LENGTH_LONG).show();
                boolean isconfirm=true;
                AddFriendAvtivity.addFrendToServerSide(mSearchFriendList.get(position).getFriendName(), isconfirm, mSearchFriendList.get(position).getUserID());
                        ((Activity) mContext).finish();
            }
        });

        return convertView;
    }

    public void addFriend(String name, String userName, String userId) {
        boolean isInsert = mdatabase.insertData(name, userName, userId);
        if (isInsert == true)
            Toast.makeText(mContext, "Friend Added Successfull", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(mContext, "Friend not added", Toast.LENGTH_LONG).show();
    }
}