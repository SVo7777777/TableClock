package com.example.screenclock;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
//import android.support.WakefulBroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


import androidx.work.WorkManager;


import com.example.screenclock.ui.notifications.NotificationsFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MyBroadcastReceiver extends BroadcastReceiver {

    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    public String data;
    public NotificationsFragment fragment;
    PowerManager powerManager;
    Boolean phone;
    private SharedPreferences prefs;
    private static final String ACTION_SEND_SMS = "com.example.screenclock.ACTION_SEND_SMS";
    @SuppressLint("SdCardPath")
    private static final String APP_SD_PATH = "/data/data/com.example.screenclock";
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
        String sFolder =  APP_SD_PATH + "/files";
        String sFile=sFolder+"/"+"phone.txt";
        String[] number = phoneFromFile(sFile);
        System.out.println(Arrays.toString(number));


        if (intentAction != null) {
            String toastMessage = "unknown intent action";
            System.out.println(" in onReceive");
            switch (intentAction){
                case Intent.ACTION_POWER_DISCONNECTED:

                    System.out.println(" in ACTION_POWER_DISCONNECTED");

                    if (Objects.equals(number[0], "number")){
                        System.out.println(number[0]);
                    }else {
                        if (Objects.equals(number[1], "on")){
                            System.out.println("ooooooonnnnn");
                            System.out.println(number[0]);
                            prefs = context.getSharedPreferences("sms_settings", Context.MODE_PRIVATE);
                            String savedSms = prefs.getString("sms", "");
                            Intent serviceIntent = new Intent();
                            serviceIntent.putExtra("phone_number", number[0]);
                            serviceIntent.putExtra("message", savedSms);
                            SmsJobIntentService.enqueueWork(context, serviceIntent);
                            //scheduleSmsAfterUnplug(context, number[0], toastMessage);
                            //sendReplySms(context, number[0], toastMessage);
                        }
                    }





                    toastMessage = "Блок питания отключен! Устройство не заряжается! ";
//
//
                    try {
                        Intent i = new Intent(context, RingtonePlayingService.class);
                        //context.startWakefulService(i);

                        context.startService(i);
                    }catch (RuntimeException e) {
                        System.out.println(e);
                        //Toast.makeText(context, (CharSequence) e, Toast.LENGTH_LONG).show();
                        Intent serviceIntent1 = new Intent(context, RingtonePlayingService.class);
                        context.startForegroundService(serviceIntent1 );
//
                    }


                    break;
                case Intent.ACTION_POWER_CONNECTED:
                    // Зарядка подключена — можно отменить запланированные задачи
                    WorkManager.getInstance(context).cancelAllWorkByTag("sms_after_unplug");
                    toastMessage = "Блок питания подключен! Устройство заряжается!";
                    break;
            }

            //Display the toast.
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "intentAction == null", Toast.LENGTH_LONG).show();
        }
    }

    public String[] phoneFromFile(String file){
        String line1 = null;
        String line2 = null;
        String[] smsonoff = new String[2];
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            String line = br.readLine();
            if (line != null) {
                line1 = line;
                line = br.readLine();
                if (line != null) {
                    line2 = line;
                }
            }
            System.out.println(line1);
            System.out.println(line2);

            smsonoff[0]=line1;
            smsonoff[1]=line2;



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return smsonoff;
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
//                    PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
//                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
//                            "MyApp::MyWakelockTag"
//                    );
//                    wakeLock.acquire();
//                    Toast.makeText(context, "включен экран", Toast.LENGTH_LONG).show();