package com.example.screenclock;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import java.io.FileNotFoundException;

public class RingtonePlayingService extends Service {
    static Ringtone r;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)

    {
        //Uri notification =Uri.parse("content://media/internal/audio/media/113");

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (notification == null) {
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }

            r = RingtoneManager.getRingtone(getBaseContext(), notification);


        //playing sound alarm
        r.play();
        System.out.println("play");

        return START_NOT_STICKY;
    }
//    @SuppressLint("ForegroundServiceType")
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        int NOTIFICATION_ID = (int) (System.currentTimeMillis()%10000);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(NOTIFICATION_ID, new Notification.Builder(this).build());
//        }
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (notification == null) {
//            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//        r = RingtoneManager.getRingtone(getBaseContext(), notification);
//        //playing sound alarm
//        r.play();
//        System.out.println("play");
//
//
//        // Do whatever you want to do here
//    }


    @Override
    public void onDestroy()
    {
        r.stop();

        System.out.println("stop");
    }
}
