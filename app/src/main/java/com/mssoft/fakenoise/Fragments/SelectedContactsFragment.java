package com.mssoft.fakenoise.Fragments;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mssoft.fakenoise.Activities.MainActivity;
import com.mssoft.fakenoise.Utilities.Permissions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mssoft.fakenoise.Adapters.SelectedContactsAdapter;
import com.mssoft.fakenoise.Constants.Constants;
import com.mssoft.fakenoise.Utilities.Contact;
import com.mssoft.fakenoise.Activities.KeypadActivity;
import com.mssoft.fakenoise.R;
import com.mssoft.fakenoise.Utilities.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Marius on 8/29/2015.
 */
public class SelectedContactsFragment extends android.app.Fragment implements ObservableScrollViewCallbacks {
    private AlertDialog mDialog;
    private Dialog audioNameDialog;
    private Context mContext;
    private ObservableListView mListView;
    private Uri mInfo;
    private View viewForSnackBar;
    private Intent mInformation;
    private Constants sharedData;
    private ArrayList<String> soundNames;
    private ArrayAdapter<String> adapter;
    private SharedPreferences.Editor edt;
    private SharedPreferences prefs;
    private Button dialogOkBtn;
    private ListView audioListView;
    private int[] dialogWidthAndHeight;
    private static String noise;
    private SelectedContactsAdapter selectedContactsAdapter;
    private String number;

    ActionBar tb;
    TextView mWelcomeText;

    public boolean isAudioFile(String name) {
        if (name.endsWith(".wav") || name.endsWith(".3gp") || name.endsWith(".mp3")
                || name.endsWith(".mp4")) {
            return true;
        }

        return false;
    }


    public ArrayList<String> getNameOfSounds() {

        ArrayList<String> strings = new ArrayList<>();
        final String NO_SOUND_FOUND = "No sound found";
        final String NO_SOUND = "No sound";

        File file = new File(Constants.AUDIO_PATH);
        File[] audioFiles = file.listFiles();

        if (audioFiles != null) {
            strings.add(NO_SOUND);
            for (int i = 0; i < audioFiles.length; i++) {
                String name = audioFiles[i].getName();
                if (isAudioFile(name)) {
                    strings.add(name);
                }
            }

        } else {
            strings.add(NO_SOUND_FOUND);
            return strings;
        }


        return strings;
    }

    public void setUpSoundDialogAdapter() {

        soundNames = getNameOfSounds();
        adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_single_choice,
                android.R.id.text1, soundNames);
        audioListView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View contacts = inflater.inflate(R.layout.selected_contacts_layout, container, false);
        viewForSnackBar = contacts.findViewById(R.id.snackbar);
        tb = ((AppCompatActivity) getActivity()).getSupportActionBar();

        sharedData = new Constants();
        addButtonFunctionalityFrom(contacts);
        mListView = (ObservableListView) contacts.findViewById(R.id.list_view);
        mWelcomeText = (TextView) contacts.findViewById(R.id.welcomeText);

        mListView.setScrollViewCallbacks(this);
        mListView.setOnItemClickListener(new ListViewOnItemClickListener());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioNameDialog = new Dialog(mContext, android.R.style.Theme_Material_Dialog);

        } else {
            audioNameDialog = new Dialog(mContext);
        }

        audioNameDialog.setContentView(R.layout.custom_dialog_layout);
        audioNameDialog.setCancelable(true);
        audioNameDialog.setTitle("Choose Sound : ");
        // Customizing the dialog's layout size
        dialogWidthAndHeight = Utils.getScreenWidthAndHeight(mContext);
        int dialogWidth = dialogWidthAndHeight[0] - dialogWidthAndHeight[0] / 5;
        int dialogHeight = dialogWidthAndHeight[1] - dialogWidthAndHeight[1] / 4;

        WindowManager.LayoutParams params = audioNameDialog.getWindow().getAttributes();
        params.width = dialogWidth;
        params.height = dialogHeight;
        audioNameDialog.getWindow().setAttributes(params);

        dialogOkBtn = (Button) audioNameDialog.findViewById(R.id.done_btn);

        audioListView = (ListView) audioNameDialog.findViewById(R.id.audio_list_view);

        //just for the first launch
        setUpSoundDialogAdapter();

        return contacts;

    }

    private class ListViewOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            number = ((TextView) view.findViewById(R.id.contact_number)).getText().toString();

            audioListView.setOnItemClickListener(new DialogListViewItemClickListener());

            dialogOkBtn.setOnClickListener(new DialogOkButtonListener());

            prefs = selectedContactsAdapter.getItemPreference(position);

            if (prefs.contains("itemPosition")) {
                Integer ItemPosition = prefs.getInt("itemPosition", 0);
                audioListView.setItemChecked(ItemPosition, true);
            }

            audioNameDialog.show();
        }
    }

    private class DialogListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            edt = prefs.edit();
            edt.putInt("itemPosition", position);
            edt.apply();

            noise = audioListView.getItemAtPosition(position).toString();
        }
    }

    private class DialogOkButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if(noise != null) {

                ContentValues values = new ContentValues();
                values.put("audio", noise);

                MainActivity.mContactsSource.update(number, values);
            }
            audioNameDialog.dismiss();
        }
    }


    private void addButtonFunctionalityFrom(View view) {

        final FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.add_btn);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Permissions.askForPermission(getActivity(),SelectedContactsFragment.this,1);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(sharedData.getSelectedContactsDialogTitle());
                builder.setSingleChoiceItems(sharedData.getSelectedContactsDialogSelectionList(), -1,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        if (Permissions.readContacts) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                                    contactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // to display contacts who have phone number
                                                    startActivityForResult(contactIntent, sharedData.getRequestContact());
                                                }
                                            }).start();
                                        } else {
                                            Snackbar.make(addButton, "Permission denied!",
                                                    Snackbar.LENGTH_LONG).show();
                                        }

                                        break;
                                    case 1:
                                        Intent keypadIntent = new Intent(getActivity(), KeypadActivity.class);
                                        startActivityForResult(keypadIntent, sharedData.getRequestKeypad());
                                        break;
                                    default:
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case Permissions.READ_CONTACTS_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Permissions.readContacts = true;
                } else {

                    Permissions.readContacts = false;

                }
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {

            setUpSoundDialogAdapter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //if database with contact list is not empty
        ArrayList<Contact> mObjectList;

        mObjectList = MainActivity.mContactsSource.getAllContacts();
        if (mObjectList.size() != 0) {

            selectedContactsAdapter = new SelectedContactsAdapter(mContext, mObjectList,
                    MainActivity.mContactsSource, mWelcomeText, tb);

            mWelcomeText.setVisibility(View.GONE);
            mListView.setAdapter(selectedContactsAdapter);
        } else {
            mObjectList.clear();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == sharedData.getRequestContact()) {
            if (resultCode == sharedData.getResultOk() && data != null) {
                mInfo = data.getData();
                if (getContactPhoneNumberAgenda().equals(sharedData.getContactError())) {
                    Snackbar.make(viewForSnackBar, sharedData.getContactError(), Snackbar.LENGTH_LONG).show();
                } else {

                    if (!MainActivity.mContactsSource.isPhoneNumberAlreadyAdded(getContactPhoneNumberAgenda())) {

                        MainActivity.mContactsSource.createContact(getContactNameAgenda(), getContactPhoneNumberAgenda());
                        Snackbar.make(viewForSnackBar, "Contact Added", Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tb.show();
                            }
                        }, 2001);
                    } else {
                        Snackbar.make(viewForSnackBar, "Number already exists!", Snackbar.LENGTH_LONG).show();
                    }
                }
            } else if (resultCode == sharedData.getResultCanceled()) {
                Snackbar.make(viewForSnackBar, "Cancelled or some error occured !", Snackbar.LENGTH_LONG).show();
            }
        } else if (requestCode == sharedData.getRequestKeypad()) {
            if (resultCode == sharedData.getResultOk()) {
                mInformation = data;
                if (!MainActivity.mContactsSource.isPhoneNumberAlreadyAdded(getContactPhoneKeypad())) {
                    MainActivity.mContactsSource.createContact(getContactNameKeypad(), getContactPhoneKeypad());
                    Snackbar.make(viewForSnackBar, "Contact Added", Snackbar.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tb.show();
                        }
                    }, 2001);
                } else {
                    Snackbar.make(viewForSnackBar, "Number already exists!", Snackbar.LENGTH_LONG).show();
                }

            } else if (resultCode == sharedData.getResultCanceled()) {
                Snackbar.make(viewForSnackBar, "Cancelled or some error occured !", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private String getContactNameKeypad() {
        return mInformation.getExtras().getString(sharedData.getSelectedContactsColumnName());
    }

    private String getContactPhoneKeypad() {
        return mInformation.getExtras().getString(sharedData.getSelectedContactsColumnPhone());
    }

    private String getContactNameAgenda() {
        Cursor cursor = mContext.getContentResolver().query(mInfo, null,
                null, null, null);

        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        cursor.close();
        return name;
    }

    private String getContactPhoneNumberAgenda() {
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
        if (cursorPhone != null) {
            cursorPhone.moveToFirst();
            String number = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursorPhone.close();
            return number;
        } else
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
        if (scrollState == ScrollState.UP) {
            if (tb.isShowing()) {
                tb.hide();
            }

        } else if (scrollState == ScrollState.DOWN) {
            if (!tb.isShowing()) {
                tb.show();
            }
        }
    }


}