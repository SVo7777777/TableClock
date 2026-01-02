package com.example.screenclock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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

public class MyBroadcastReceiver extends BroadcastReceiver {

    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    public String data;
    public NotificationsFragment fragment;

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

        if (intentAction != null) {
            String toastMessage = "unknown intent action";
            switch (intentAction){
                case Intent.ACTION_POWER_DISCONNECTED:
                    toastMessage = "Блок питания отключен! Устройство не заряжается! Возможно нет электричества";
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        context.startForegroundService(new Intent(context, RingtonePlayingService.class));
//                    } else {
//                        context.startService(new Intent(context, RingtonePlayingService.class));
//                    }
                    try {
                        Intent i = new Intent(context, RingtonePlayingService.class);
                        //context.startForegroundService(i);
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
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, "intentAction == null", Toast.LENGTH_LONG).show();
        }
    }
}