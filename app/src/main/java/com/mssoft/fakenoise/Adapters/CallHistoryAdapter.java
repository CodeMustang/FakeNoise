package com.mssoft.fakenoise.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mssoft.fakenoise.R;
import com.mssoft.fakenoise.Utilities.ContactLog;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Marius on 9/24/2016.
 */

public class CallHistoryAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ContactLog> mLogs;

    public CallHistoryAdapter(Context context, ArrayList<ContactLog> logs) {
        mContext = context;
        mLogs = logs;
    }

    @Override
    public int getCount() {
        return mLogs.size();
    }

    @Override
    public Object getItem(int position) {
        return mLogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.log_item_layout, parent, false);

            holder.contactName = (TextView) convertView.findViewById(R.id.c_name);
            holder.contactPhone = (TextView) convertView.findViewById(R.id.c_phone);
            holder.time = (TextView) convertView.findViewById(R.id.c_time);
            holder.noise = (TextView) convertView.findViewById(R.id.c_noise);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.noise.setText(mLogs.get(position).getNoise());
        holder.contactName.setText(mLogs.get(position).getName());
        holder.contactPhone.setText(mLogs.get(position).getPhone());
        holder.time.setText(mLogs.get(position).getTime());

        return convertView;
    }

    private static class Holder {
        TextView contactName;
        TextView contactPhone;
        TextView time;
        TextView noise;
    }
}
