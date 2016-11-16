package com.mssoft.fakenoise.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mssoft.fakenoise.R;
import com.mssoft.fakenoise.Tasks.RecordTask;
import com.mssoft.fakenoise.Utilities.Permissions;

/**
 * Created by Marius on 7/17/2016.
 */
public class RecordNoiseActivity extends Activity {

    private TextView title;
    private String titleText;
    private static boolean recordPressed;
    private ImageView recordStartImg;
    private Chronometer chronometer;
    private RecordTask task;
    private FloatingActionButton recordBtn;
    private View snackbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);

        snackbarView = findViewById(R.id.snckbar);
        recordStartImg = (ImageView) findViewById(R.id.record_start);
        recordBtn = (FloatingActionButton) findViewById(R.id.record_btn);
        title = (TextView) findViewById(R.id.title);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        titleText = getString(R.string.record_default_title);
        title.setText(titleText);
        recordStartImg.setVisibility(View.GONE);

        recordPressed = false;

    }

    public void processRecordingTask(){

        if (!recordPressed) {


            //start name dialog
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            final View dialogView = inflater.inflate(R.layout.name_dialog, null);
            dialogBuilder.setView(dialogView);

            final String[] mName = new String[1];
            final EditText edt = (EditText) dialogView.findViewById(R.id.name_edt);

            dialogBuilder.setTitle("Name your noise");
            dialogBuilder.setMessage("Please enter a name");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //do nothing - for older versions of android
                }
            });

            final AlertDialog noiseNameDialog = dialogBuilder.create();
            noiseNameDialog.show();

            //the listener in which we can control whenever we dismiss the dialog or not.
            noiseNameDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (edt.getText().toString().isEmpty()) {
                        Snackbar.make(snackbarView, "Please enter a name !", Snackbar.LENGTH_SHORT).show();

                    } else {
                        recordPressed = true;
                        mName[0] = edt.getText().toString();
                        task = new RecordTask(title, titleText, recordStartImg, chronometer, recordBtn,
                                getApplicationContext(), mName[0]);
                        task.execute();

                        noiseNameDialog.dismiss();
                    }

                }
            });

        } else {
            try {
                task.stopCapture();
                recordPressed = false;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public void capture(View v) {

        Permissions.askForPermission(this,null,3);

        if (Permissions.writeToDisk) {
           processRecordingTask();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case Permissions.STORAGE_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Permissions.writeToDisk = true;
                    processRecordingTask();

                } else {

                    Permissions.writeToDisk = false;

                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recordPressed) {
            task.stopCapture();
        }
    }
}