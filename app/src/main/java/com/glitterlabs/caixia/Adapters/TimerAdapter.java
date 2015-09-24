package com.glitterlabs.caixia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.glitterlabs.caixia.R;


/**
 * Created by mohinish on 8/8/15.
 */
public class TimerAdapter extends BaseAdapter {

    Context mContext;
    String times[];
    public TimerAdapter(Context context,String times[]) {
        this.mContext=context;
        this.times=times;
    }

    @Override
    public int getCount() {
        return times.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.timer_list_item,null);
        }

        TextView tvTime= (TextView) convertView.findViewById(R.id.tvTime_TimerListItem);
        tvTime.setText(times[position]);

        return convertView;
    }
}
