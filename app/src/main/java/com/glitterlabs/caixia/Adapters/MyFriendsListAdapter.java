package com.glitterlabs.caixia.Adapters;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.glitterlabs.caixia.Databases.DatabseHelper;
import com.glitterlabs.caixia.Models.Friend;
import com.glitterlabs.caixia.R;

import java.util.ArrayList;
import java.util.List;


public class MyFriendsListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Friend> mlist = new ArrayList<Friend>();
    public ArrayList<String> checkedFrends = new ArrayList<String>();
    private PopupWindow popupWindow;

    public MyFriendsListAdapter(Context context, ArrayList allFrends) {

        this.mContext = context;
        this.mlist = allFrends;

    }

    @Override
    public int getCount() {
        return mlist.size();
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


        convertView = LayoutInflater.from(mContext).inflate(R.layout.my_friends_list_item, null);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvMyFriendListItem_Name);
        tvName.setText(mlist.get(position).getFriendName());
        ImageView ivMenu= (ImageView) convertView.findViewById(R.id.ivMyFriendListItem_menu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow popupWindowDogs = popupWindowDogs(mlist.get(position).getUserID());
                popupWindowDogs.showAsDropDown(v, -5 , 0);
            }
        });
        return convertView;
    }

    public PopupWindow popupWindowDogs(final String userId) {
        List<String> dogsList = new ArrayList<String>();
        dogsList.add("Delete");
        // initialize a pop up window type
        popupWindow = new PopupWindow(mContext);
        // the drop down list is a list view
        ListView listViewDogs = new ListView(mContext);
        ArrayAdapter adapter= new ArrayAdapter(mContext,R.layout.text_item,dogsList);
        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(adapter);
        listViewDogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabseHelper mdatabase = new DatabseHelper(mContext);
                mdatabase.deleteItem(userId);
                popupWindow.dismiss();
            }
        });
        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(200);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);
        return popupWindow;
    }

}