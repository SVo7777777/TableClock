package com.example.screenclock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.screenclock.databinding.FragmentNotificationsBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public Ringtone ringtone;
    public Vibrator vibrator;
    TextView data;
    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    // implement onReceive() method
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equalsIgnoreCase("android.intent.action.ACTION_POWER_DISCONNECTED")) {
//            String message = "Обнаружено сообщение "
//                    + intent.getAction();
//
//            Toast.makeText(context, message,
//                    Toast.LENGTH_LONG).show();
//        }
        String intentAction = intent.getAction();
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy H:mm:ss");

        calendar.set(current_year, current_month, current_day);
        String sDate_now = sdf1.format(calendar.getTime());
        System.out.println(sDate_now);
       //data = context.FragmentNotificationsBinding().textveiw2;


        if (intentAction != null) {
            String toastMessage = "unknown intent action";
            switch (intentAction){
                case Intent.ACTION_POWER_DISCONNECTED:
                    toastMessage = "Power connected!";
                    vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(4000);

                    Toast.makeText(context, "Блок питания отключен! Устройство не заряжается! Возможно нет электричества", Toast.LENGTH_LONG).show();
                    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    if (alarmUri == null) {
                        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }

                    // setting default ringtone
                    ringtone = RingtoneManager.getRingtone(context, alarmUri);

                    // play ringtone
                    ringtone.play();
                    break;
                case Intent.ACTION_POWER_CONNECTED:
                    toastMessage = "Блок питания подключен! Устройство заряжается!";
                    if ((ringtone != null) && (vibrator != null)){
                        ringtone.stop();
                        vibrator.cancel();
                        System.out.println("here");
                    }

                    break;
            }

            //Display the toast.
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, "intentAction == null", Toast.LENGTH_LONG).show();
        }

//        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
//            // your code when power connected
//            Toast.makeText(context, "power connected! зарядка включена! электричество есть!", Toast.LENGTH_LONG).show();
//
//        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
//            // your code when power disconnected
//            // we will use vibrator first
//            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//            vibrator.vibrate(4000);
//
//            Toast.makeText(context, "power disconnected! зарядка отключена! электричество нет!", Toast.LENGTH_LONG).show();
//            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//            if (alarmUri == null) {
//                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            }
//
//            // setting default ringtone
//            Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
//
//            // play ringtone
//            ringtone.play();
//        }


    }
}