package com.example.screenclock;



import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
//import android.support.WakefulBroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;


import com.example.screenclock.ui.notifications.NotificationsFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MyBroadcastReceiver extends BroadcastReceiver {

    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    public String data;
    public NotificationsFragment fragment;
    PowerManager powerManager;

    @SuppressLint("WakelockTimeout")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    // implement onReceive() method
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy H:mm:ss");
        calendar.set(current_year, current_month, current_day);
        String sDate_now = sdf1.format(calendar.getTime());
        System.out.println(sDate_now);
        data = sDate_now;
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (intentAction != null) {
            String toastMessage = "unknown intent action";
            switch (intentAction){
                case Intent.ACTION_POWER_DISCONNECTED:
                    //turnOnScreen();
//                    PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
//                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
//                            "MyApp::MyWakelockTag"
//                    );
//                    wakeLock.acquire();
//                    Toast.makeText(context, "включен экран", Toast.LENGTH_LONG).show();



                    toastMessage = "Блок питания отключен! Устройство не заряжается! Возможно нет электричества";
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        context.startForegroundService(new Intent(context, RingtonePlayingService.class));
//                    } else {
//                        context.startService(new Intent(context, RingtonePlayingService.class));
//                    }
                    try {
                        Intent i = new Intent(context, RingtonePlayingService.class);
                        //context.startWakefulService(i);

                        context.startService(i);
                    }catch (RuntimeException e) {
                        System.out.println(e);
                        //Toast.makeText(context, (CharSequence) e, Toast.LENGTH_LONG).show();
                        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
                        context.startForegroundService(serviceIntent );
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            context.startForegroundService(serviceIntent);
//                        } else {
//                            context.startService(serviceIntent);
//                        }
                    }
                    //Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(context, RingtonePlayingService.class);
//                    context.startService(i);

                    //context.startService(startIntent);
                    break;
                case Intent.ACTION_POWER_CONNECTED:
                    toastMessage = "Блок питания подключен! Устройство заряжается!";
//                    Intent i2= new Intent(context, RingtonePlayingService.class);
//                    context.stopService(i2);

                    break;



            }

            //Display the toast.
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "intentAction == null", Toast.LENGTH_LONG).show();
        }


    }
    @SuppressLint("WakelockTimeout")
    private void turnOnScreen() {
        System.out.println("включаем экран");

        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
                "MyApp::MyWakelockTag"
        );

        wakeLock.acquire();
    }
}