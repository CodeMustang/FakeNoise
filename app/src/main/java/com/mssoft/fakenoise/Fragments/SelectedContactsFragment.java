package com.mssoft.fakenoise.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mssoft.fakenoise.Adapters.SelectedContactsAdapter;
import com.mssoft.fakenoise.Constants.Contants;
import com.mssoft.fakenoise.Contact;
import com.mssoft.fakenoise.Database.ContactsDataSource;
import com.mssoft.fakenoise.KeypadActivity;
import com.mssoft.fakenoise.R;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Marius on 8/29/2015.
 */
public class SelectedContactsFragment extends Fragment implements ObservableScrollViewCallbacks {
    private AlertDialog mDialog;
    private Context mContext;
    private ObservableListView mListView;
    private Uri mInfo;
    private View viewForSnackBar;
    private ContactsDataSource mSource;
    private Intent mInformation;
    private com.mssoft.fakenoise.Constants.Contants sharedData;
    public static ActionBar tb;
    public static TextView  mWelcomeText;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contacts = inflater.inflate(R.layout.selected_contacts_layout, container, false);
        viewForSnackBar = contacts.findViewById(R.id.snackbar);
        tb = ((AppCompatActivity)getActivity()).getSupportActionBar();
        mSource = new ContactsDataSource(mContext);
        try {
            mSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sharedData = new Contants();
        addButtonFunctionalityFrom(contacts);
        mListView = (ObservableListView) contacts.findViewById(R.id.list_view);
        mWelcomeText = (TextView) contacts.findViewById(R.id.welcomeText);

        mListView.setScrollViewCallbacks(this);

        return contacts;

    }

    private void addButtonFunctionalityFrom(View view){

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.add_btn);
        addButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.add_btn_text)));
        addButton.setRippleColor(R.color.element_bg);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(sharedData.getSelectedContactsDialogTitle());
                builder.setSingleChoiceItems(sharedData.getSelectedContactsDialogSelectionList(), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                        contactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // to display contacts who have phone number
                                        startActivityForResult(contactIntent, sharedData.getRequestContact());
                                    }
                                }).start();
                                break;
                            case 1:
                                Intent keypadIntent = new Intent(getActivity(), KeypadActivity.class);
                                startActivityForResult(keypadIntent, sharedData.getRequestKeypad());
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                mDialog = builder.create();
                mDialog.show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        //if database with contact list is not empty
        ArrayList<Contact> mObjectList = new ArrayList<>();
        mObjectList = mSource.getAllContacts();
            if(mObjectList.size() != 0) {
                SelectedContactsAdapter adapter = new SelectedContactsAdapter(mContext, mObjectList, mSource);
                mWelcomeText.setVisibility(View.GONE);
                mListView.setAdapter(adapter);
            }
        else
                mObjectList.clear();
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == sharedData.getRequestContact()){
            if(resultCode == sharedData.getResultOk() && data != null){
                mInfo = data.getData();
                if(getContactPhoneNumberAgenda().equals(sharedData.getContactError())){
                    Toast.makeText(mContext,sharedData.getContactError(),Toast.LENGTH_SHORT).show();
                }
                else {
                    mSource.createContact(getContactNameAgenda(), getContactPhoneNumberAgenda());
                    Snackbar.make(viewForSnackBar, "Contact Added", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tb.show();
                        }
                    }, 2001);
                }
            }
            else if (resultCode == sharedData.getResultCanceled()){
                Toast.makeText(getActivity(),"CANCELLED OR SOME ERROR OCCURRED !",Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == sharedData.getRequestKeypad()){
            if(resultCode == sharedData.getResultOk()){
                mInformation = data;
                mSource.createContact(getContactNameKeypad(), getContactPhoneKeypad());
                Snackbar.make(viewForSnackBar, "Contact Added", Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tb.show();
                    }
                },2001);

            }
            else if (resultCode == sharedData.getResultCanceled()){
                Toast.makeText(getActivity().getApplicationContext(),"CANCELLED OR SOME ERROR OCCURRED !",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getContactNameKeypad(){
        return mInformation.getExtras().getString(sharedData.getSelectedContactsColumnName());
    }

    private String getContactPhoneKeypad(){
        return mInformation.getExtras().getString(sharedData.getSelectedContactsColumnPhone());
    }

    private String getContactNameAgenda(){
        Cursor cursor = mContext.getContentResolver().query(mInfo, null,
                null, null, null);

        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        cursor.close();
        return name;
    }

    private String getContactPhoneNumberAgenda(){
        Cursor cursor = mContext.getContentResolver().query(mInfo, new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        cursor.moveToFirst();
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        cursor.close();

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone._ID + " = ? AND " +  // Phone._ID is for the database ; Phone.CONTACT_ID is for contact when you are not querying that table
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{id},
                null);
        if(cursorPhone!=null) {
            cursorPhone.moveToFirst();
            String number = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursorPhone.close();
            return number;
        }
        else
            return sharedData.getContactError();
    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if(scrollState == ScrollState.UP){
            if(tb.isShowing()){
                tb.hide();
            }

        }
        else if(scrollState == ScrollState.DOWN){
            if(!tb.isShowing()){
                tb.show();
            }
        }
    }
}