package com.example.screenclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Перерегистрация приёмника питания
            IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
            context.registerReceiver(new MyBroadcastReceiver(), filter);
        }
    }
}
