package com.example.screenclock;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private String TAG = this.getClass().getSimpleName();
    static Ringtone r;

    @Override
    public void onCreate() {
        Log.d(TAG, "Inside onCreate() API");
        System.out.println("mi zdesj");
        if (Build.VERSION.SDK_INT >= 26) {
            System.out.println("mi zdesj if");
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
            mBuilder.setContentTitle("Notification Alert, Click Me!");
            mBuilder.setContentText("Hi, This is Android Notification Detail!");
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
           // mNotificationManager.notify(100, mBuilder.build());
            startForeground(100, mBuilder.getNotification());


        }else {
            System.out.println("mi zdesj else");
        }
    }


    @Override
    public int onStartCommand(Intent resultIntent, int resultCode, int startId) {
        Log.d(TAG, "inside onStartCommand() API");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (notification == null) {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        r = RingtoneManager.getRingtone(getBaseContext(), notification);
        //playing sound alarm
        r.play();
        System.out.println("play my service");

        return startId;
    }


    @Override
    public void onDestroy() {
        r.stop();
        System.out.println("stop my service");
        Log.d(TAG, "inside onDestroy() API");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


}