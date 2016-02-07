package com.mssoft.fakenoise.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mssoft.fakenoise.Contact;
import com.mssoft.fakenoise.Database.ContactsDataSource;
import com.mssoft.fakenoise.Fragments.SelectedContactsFragment;
import com.mssoft.fakenoise.R;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Marius on 9/17/2015.
 */
public class SelectedContactsAdapter extends BaseAdapter {
    private Context mContext;
    private Button delRow;
    private ContactsDataSource source;
    private ArrayList<Contact> mObjectList,tempList;
    public SelectedContactsAdapter(Context context , ArrayList<Contact> mObjectList, ContactsDataSource source)  {
        mContext= context;
        this.mObjectList = mObjectList;
        this.source = source;
        tempList = mObjectList;
    }
    @Override
    public int getCount() {
        return mObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class Holder{
        TextView mName;
        TextView mNumber;
        TextView mImg;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.contact_item_layout,parent,false);

            holder.mName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.mNumber = (TextView) convertView.findViewById(R.id.contact_number);
            holder.mImg = (TextView) convertView.findViewById(R.id.contact_count);
            delRow = (Button) convertView.findViewById(R.id.deleteRow);

            convertView.setTag(holder); //store the holder with the view
        }
        else{
            //avoid calling findViewById
            holder = (Holder)convertView.getTag();
        }
        holder.mName.setText(mObjectList.get(position).getName());
        holder.mNumber.setText(mObjectList.get(position).getPhone());

        holder.mImg.setText("# "+String.valueOf(position + 1));
        holder.mImg.setTextColor(ContextCompat.getColor(mContext, R.color.contact_item_color));

        delRow.setTag(position);
        delRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    source.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                source.deleteContact(tempList.get(position));
                mObjectList.remove(((int) v.getTag()));
                SelectedContactsAdapter.this.notifyDataSetChanged();

                if(tempList.isEmpty()){
                    SelectedContactsFragment.mWelcomeText.setVisibility(View.VISIBLE);
                }

            }
        });

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        SelectedContactsFragment.tb.show();
    }

}
