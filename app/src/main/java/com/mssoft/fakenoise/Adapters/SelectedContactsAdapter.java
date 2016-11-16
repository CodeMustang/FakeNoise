package com.mssoft.fakenoise.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mssoft.fakenoise.Utilities.Contact;
import com.mssoft.fakenoise.Database.ContactsDataSource;
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
    private ArrayList<Contact> mContacts, tempList;
    private String number, name;
    private SharedPreferences[] itemPreference;
    private ActionBar tb;
    private TextView mWelcomeText;

    public SelectedContactsAdapter(Context context, ArrayList<Contact> mContacts,
                                   ContactsDataSource source , TextView mWelcomeText,ActionBar tb) {
        this.mWelcomeText = mWelcomeText;
        this.tb = tb;
        mContext = context;
        this.mContacts = mContacts;
        this.source = source;
        tempList = mContacts;
        itemPreference = new SharedPreferences[mContacts.size()];
    }

    private static class Holder {
        TextView mName;
        TextView mNumber;
        TextView mImg;
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return mContacts.get(position).getId();
    }

    public String getNumber() {
        return number;
    }

    public String getName(){
        return name;
    }

    public SharedPreferences getItemPreference(int position){
        return itemPreference[position];
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.contact_item_layout, parent, false);

            holder.mName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.mNumber = (TextView) convertView.findViewById(R.id.contact_number);
            holder.mImg = (TextView) convertView.findViewById(R.id.contact_count);
            delRow = (Button) convertView.findViewById(R.id.deleteRow);

            convertView.setTag(holder); //store the holder with the view
        } else {
            //avoid calling findViewById
            holder = (Holder) convertView.getTag();
        }
        holder.mName.setText(mContacts.get(position).getName());
        holder.mNumber.setText(mContacts.get(position).getPhone());

        number = holder.mNumber.getText().toString();
        name = holder.mName.getText().toString();

        holder.mImg.setText("# " + String.valueOf(position + 1));
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
                mContacts.remove(((int) v.getTag()));
                SelectedContactsAdapter.this.notifyDataSetChanged();

                if (tempList.isEmpty()) {
                    mWelcomeText.setVisibility(View.VISIBLE);
                }

            }
        });

        itemPreference[position] = mContext.getSharedPreferences("item " + getItemId(position),
                Context.MODE_PRIVATE);

        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        tb.show();
    }

}
