package com.example.screenclock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;

public class ScreenReceiver extends BroadcastReceiver {

    private boolean screenOff;
    PowerManager powerManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    turnOnScreen();
                }
            }, 50000);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;
        }

//        Intent i = new Intent(context, UpdateService.class);
//        i.putExtra("screen_state", screenOff);
//        context.startService(i);
    }
    @SuppressLint("WakelockTimeout")
    private void turnOnScreen() {
        System.out.println("включаем экран");
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
                "MyApp::MyWakelockTag"
        );

        wakeLock.acquire();
    }

}