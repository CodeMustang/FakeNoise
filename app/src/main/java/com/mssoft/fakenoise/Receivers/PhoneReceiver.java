package com.mssoft.fakenoise.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.mssoft.fakenoise.Listeners.PhoneStageListener;

/**
 * Created by Marius on 9/20/2016.
 */
public class PhoneReceiver extends BroadcastReceiver {

    static PhoneStageListener phoneStageListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(phoneStageListener == null) {
            phoneStageListener = new PhoneStageListener(context);
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStageListener,PhoneStateListener.LISTEN_CALL_STATE);

    }


}
