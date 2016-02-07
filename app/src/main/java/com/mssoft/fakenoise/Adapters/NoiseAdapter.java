package com.mssoft.fakenoise.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mssoft.fakenoise.NoiseSounds;
import com.mssoft.fakenoise.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Marius on 9/28/2015.
 */
public class NoiseAdapter extends BaseAdapter {

    private ArrayList<NoiseSounds> list = new ArrayList<>();
    private Context context;

    public NoiseAdapter(Context context, ArrayList<NoiseSounds> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class Holder {
        ImageView img;
        TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.grid_noise_layout, parent, false);

            holder.img = (ImageView) convertView.findViewById(R.id.noise_image);
            holder.tv = (TextView) convertView.findViewById(R.id.noise_text);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.img.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_noise_bg));
        holder.tv.setText(list.get(position).getName());


        return convertView;
    }
}