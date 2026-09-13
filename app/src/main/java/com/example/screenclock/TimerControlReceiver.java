package com.example.screenclock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class TimerControlReceiver extends BroadcastReceiver {
//    public static final String ACTION_STOP_TIMER = "com.example.myapp.ACTION_STOP_TIMER";
//    public static final String EXTRA_TIMER_ID = "timer_id";
    public static final String ACTION_STOP_TIMER = "ACTION_STOP_TIMER";
    public static final String EXTRA_TIMER_ID = "timer_id";

    @Override
    public void onReceive(Context context, Intent intent) {



            Log.d("TimerControl", "onReceive: " + intent.getAction());

//            if (ACTION_STOP_TIMER.equals(intent.getAction())) {
//                // Останавливаем сервис со звуком → onDestroy → звук прекращается, уведомление убирается
//                //context.stopService(new Intent(context, AlarmSoundService.class));
//                Intent stopServiceIntent = new Intent(context, AlarmSoundService.class)
//                        .putExtra("action", "stop");
//                ContextCompat.startService(context, stopServiceIntent);
//            }

            if (ACTION_STOP_TIMER.equals(intent.getAction())) {
                Intent stopIntent = new Intent(context, AlarmSoundService.class)
                        .putExtra("action", "stop");
                context.startService(stopIntent);
            }


    }
    }



