package com.glitterlabs.caixia.Adapters;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.glitterlabs.caixia.MessageDisplayActivity;
import com.glitterlabs.caixia.Models.InboxList;
import com.glitterlabs.caixia.R;

import java.util.ArrayList;


public class InboxAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<InboxList> mInboxListData = new ArrayList<InboxList>();
    private TextView tvName,tvText,tvCurrentTime;
    public InboxAdapter(Context context,ArrayList<InboxList> inboxListData) {
        this.mContext=context;
        this.mInboxListData = inboxListData;
    }

    @Override
    public int getCount() {
        return mInboxListData.size();
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
    public View getView(final int position,  View convertView, ViewGroup parent) {


        convertView= LayoutInflater.from(mContext).inflate(R.layout.list_item,null);
        tvName = (TextView) convertView.findViewById(R.id.tvUserName_ListItem);
        if (0 == mInboxListData.get(position).getStatus()){
            tvName.setText(mInboxListData.get(position).getSenderName());
            tvName.setTypeface(tvName.getTypeface(), Typeface.BOLD);
            //only unseen message can display
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(mContext, MessageDisplayActivity.class);
                    i.putExtra("id", mInboxListData.get(position).getItemId());
                    i.putExtra("text", mInboxListData.get(position).getTextMessage());
                    mContext.startActivity(i);
                }
            });
        }else{
            tvName.setText(mInboxListData.get(position).getSenderName());
        }


        tvText = (TextView) convertView.findViewById(R.id.tvText_ListItem);
        tvText.setText(mInboxListData.get(position).getTextMessage());
        tvCurrentTime= (TextView) convertView.findViewById(R.id.tvTime_ListItem);
        tvCurrentTime.setText(mInboxListData.get(position).getCurrentTime());


        return convertView;
    }
}