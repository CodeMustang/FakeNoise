package com.mssoft.fakenoise.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.mssoft.fakenoise.Activities.MainActivity;
import com.mssoft.fakenoise.Constants.Constants;
import com.mssoft.fakenoise.Receivers.PhoneReceiver;
import com.mssoft.fakenoise.R;

/**
 * Created by Marius on 9/12/2016.
 */
public class PhoneListenerService extends Service{

    private final int NOTIFICATION = 1; //unique identifier for notification

    public static boolean isRunning = false;
    public static PhoneListenerService instance = null;

    private NotificationManager notificationManager = null;
    private PhoneReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        instance = this;
        isRunning = true;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        startListening();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.megaphone)
                .setTicker("Fake Noise is Running...")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Fake Noise")
                .setContentText("Listening for incoming phone calls...")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        //start service in foreground mode
        startForeground(NOTIFICATION, notification);

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        isRunning = false;
        instance = null;

        notificationManager.cancel(NOTIFICATION);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void startListening(){

        receiver = new PhoneReceiver();
        IntentFilter filter = new IntentFilter(Constants.BROADCAST);
        this.registerReceiver(receiver, filter);

        Intent intent = new Intent(Constants.BROADCAST);
        sendBroadcast(intent);
    }
}
