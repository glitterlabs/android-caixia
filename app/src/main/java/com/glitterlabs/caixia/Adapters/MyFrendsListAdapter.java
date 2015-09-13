package com.glitterlabs.caixia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.glitterlabs.caixia.Models.Friend;
import com.glitterlabs.caixia.R;

import java.util.ArrayList;


/**
 * Created by mohinish on 8/23/15.
 */
public class MyFrendsListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Friend> mlist = new ArrayList<Friend>();
    public ArrayList<String> checkedFrends = new ArrayList<String>();


    public MyFrendsListAdapter(Context context, ArrayList allFrends) {

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
        TextView tvName = (TextView) convertView.findViewById(R.id.tvMyFriendName_listItem);
        tvName.setText(mlist.get(position).getFriendName());
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cbMyFriendName_listItem);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedFrends.add(position, mlist.get(position).getUserID());
                }
            }
        });


        /*checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    checkedFrends.add(position,mlist.get(position).getUserID());
                }
            }
        });*/

        return convertView;
    }
}
