package com.mssoft.fakenoise.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by Marius on 8/22/2016.
 */

abstract public class Permissions {

    public static final int READ_CONTACTS_REQUEST_CODE = 1;
    public static final int RECORD_AUDIO_REQUEST_CODE = 2;
    public static final int STORAGE_REQUEST_CODE = 3;
    public static final int PHONE_STATE_REQUEST_CODE = 4;
    public static final int MODIFY_AUDIO_SETTINGS_REQUEST_CODE = 5;
    private static String message = "";

    public static boolean readContacts = false, recordAudio = false, writeToDisk = false,
            readPhoneState = false;

    private static void explainPermission(String message, DialogInterface.OnClickListener listener,
                                          Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private static boolean checkPermission(Activity activity, final int CODE) {

        switch (CODE) {

            case 1:

                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    message = "You need to Read Contacts";

                    return true;
                }
                break;

            case 2:

                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    message = " You need to Record Audio";

                    return true;
                }
                break;

            case 3:

                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&

                        ContextCompat.checkSelfPermission(activity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    message = " You need to approve reading and writing to sd storage, for saving " +
                            "the recorded audio file";

                    return true;
                }

                break;

            case 4:

                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    message = " You need to Read Phone State , in order to know whether the incoming" +
                            "phone number is chosen for the background sound or not.";

                    return true;
                }
                break;
        }

        return false;

    }

    private static void manageRequests(Activity activity, android.app.Fragment fragment, final int CODE){

        switch (CODE) {
            case 1:
                FragmentCompat.requestPermissions(fragment,new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACTS_REQUEST_CODE);
                break;

            case 2:
                FragmentCompat.requestPermissions(fragment, new String[]{Manifest.permission.RECORD_AUDIO},
                        RECORD_AUDIO_REQUEST_CODE);
                break;

            case 3:
                ActivityCompat.requestPermissions(activity, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_REQUEST_CODE);
                break;

            case 4:
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE},
                        PHONE_STATE_REQUEST_CODE);
                break;

        }
    }

    private static String getStringFromCodePermission(final int CODE){

        switch (CODE){
            case 1:
                return Manifest.permission.READ_CONTACTS;
            case 2:
                return Manifest.permission.RECORD_AUDIO;
            case 3:
                return Manifest.permission.WRITE_EXTERNAL_STORAGE;
            case 4:
                return Manifest.permission.READ_PHONE_STATE;
            default:
                return "";
        }

    }

    public static void askForPermission(Activity activity, final android.app.Fragment fragment, final int CODE) {

        if (checkPermission(activity, CODE)) {

            final Activity fActivity = activity;

            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    getStringFromCodePermission(CODE)) && (CODE != 1 && CODE != 2)) {

                explainPermission(message,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                manageRequests(fActivity,fragment,CODE);

                            }
                        }, activity);

                return;
            }

            manageRequests(fActivity,fragment,CODE);

        }

        else{

            switch (CODE){
                case 1:
                    readContacts = true;
                    break;
                case 2:
                    recordAudio = true;
                    break;
                case 3:
                    writeToDisk = true;
                    break;
                case 4:
                    readPhoneState = true;
                    break;
            }
        }

    }

}