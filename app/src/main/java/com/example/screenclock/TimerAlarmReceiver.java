package com.example.screenclock;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import android.media.MediaPlayer;
import android.os.IBinder;

public class TimerAlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Просто запускаем сервис, который будет играть звук
        Intent serviceIntent = new Intent(context, AlarmSoundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }

    private void showNotification(Context context) {
        String channelId = "timer_channel";
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String timerId = String.valueOf(System.currentTimeMillis()); // уникальный ID
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Таймер", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }
        // Intent для кнопки «Стоп» в уведомлении
//        Intent stopIntent = new Intent(TimerControlReceiver.ACTION_STOP_TIMER);
//        stopIntent.putExtra(TimerControlReceiver.EXTRA_TIMER_ID, timerId);
        // Стало (работает всегда):
        Intent stopIntent = new Intent(context, TimerControlReceiver.class);
        stopIntent.putExtra(TimerControlReceiver.EXTRA_TIMER_ID, timerId);
        stopIntent.setAction(TimerControlReceiver.ACTION_STOP_TIMER); // для логики внутри ресивера
        PendingIntent stopPending = PendingIntent.getBroadcast(
                context,
                1001, // requestCode должен быть уникальным
                stopIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Таймер")
                .setContentText("Таймер работает…")
                .setOngoing(true) // уведомление нельзя смахнуть
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .addAction(
                        android.R.drawable.ic_menu_close_clear_cancel,
                        "Стоп",
                        stopPending);

        nm.notify(1001, builder.build());
    }


    // Важно: освобождаем ресурсы, если приложение всё ещё «живёт»
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }




}

