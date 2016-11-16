package com.mssoft.fakenoise.Listeners;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.mssoft.fakenoise.Activities.MainActivity;
import com.mssoft.fakenoise.Constants.Constants;
import com.mssoft.fakenoise.Database.ContactsDataSource;
import com.mssoft.fakenoise.Utilities.Contact;
import com.mssoft.fakenoise.Utilities.Permissions;
import com.mssoft.fakenoise.Utilities.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Marius on 9/13/2016.
 */
public class PhoneStageListener extends android.telephony.PhoneStateListener {

    private Context mContext;
    private AudioManager audioManager;
    private ContactsDataSource mNoises;
    private String contactName, contactNumber, noise, path;
    private Boolean isCallEstablished = false, isContactFound = false;
    private String audioPath;
    private MediaPlayer player;

    public PhoneStageListener(Context context) {
        this.mContext = context;
        player = new MediaPlayer();
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {

            case TelephonyManager.CALL_STATE_RINGING:
                mNoises = MainActivity.mContactsSource;

                Contact contactRequired = findContactWhoMeetsReq(incomingNumber,
                        mNoises.getAllContacts()
                );

                if (contactRequired != null) {

                    contactName = contactRequired.getName();
                    contactNumber = contactRequired.getPhone();
                    noise = contactRequired.getNoise();
                    path = contactRequired.getPath();

                    Calendar cal = Calendar.getInstance();
                    String time = cal.get(Calendar.HOUR) + " : " + cal.get(Calendar.MINUTE);

                    MainActivity.mLogsDataSource.createLog(contactName, contactNumber, noise, time);

                    isContactFound = true;
                }

                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:

                if (isContactFound) {

                    isCallEstablished = true;

                    audioPath = Constants.AUDIO_PATH + path;

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    audioManager = (AudioManager) mContext
                            .getSystemService(Context.AUDIO_SERVICE);

                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.setSpeakerphoneOn(true);
//                    playAudioFromPath(audioPath);

                    isContactFound = false;
                }

                break;

            case TelephonyManager.CALL_STATE_IDLE:

                if (isCallEstablished) {

                    isCallEstablished = false;
                    audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.setSpeakerphoneOn(false);
                    if (player.isPlaying()) {
                        player.stop();
                    }

                    player.release();
                }

                break;
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                try {
                    mp.setDataSource(mContext, Uri.parse(audioPath));
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Contact findContactWhoMeetsReq(String phoneNumber, ArrayList<Contact> contacts) {

        for (int i = 0; i < contacts.size(); i++) {

            if (Utils.getCleanPhoneNumber(contacts.get(i).getPhone()).equals(
                    Utils.getCleanPhoneNumber(phoneNumber))
                    ) {
                return contacts.get(i);
            }
        }

        return null;
    }

    private void playAudioFromPath(final String path) {

        try {
            player.setDataSource(mContext, Uri.parse(path));
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Something went wrong with the audio file!", Toast.LENGTH_SHORT).
                    show();
        }
    }
}
