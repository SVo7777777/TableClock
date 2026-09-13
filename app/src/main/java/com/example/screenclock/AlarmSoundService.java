package com.example.screenclock;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
public class AlarmSoundService extends Service {
    private MediaPlayer mediaPlayer;
    private static final String CHANNEL_ID = "alarm_channel";
    private static final int NOTIF_ID = 1001;

    //@Override
    public int onStartCommand0(Intent intent, int flags, int startId) {
        createNotificationChannel();

        Notification notification = buildNotification();
        startForeground(NOTIF_ID, notification);

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm_tone);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }

        return START_STICKY;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 1. Сначала всегда вызываем startForeground — независимо от action
        createNotificationChannel();
        Notification notification = buildNotification();
        startForeground(NOTIF_ID, notification);


        // 2. Проверяем intent на null (система может перезапустить с null)
        String action = (intent != null) ? intent.getStringExtra("action") : null;

        if ("stop".equals(action)) {
            handleStopCommand();
            return START_NOT_STICKY;
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm_tone);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }
        // тут логика запуска таймера, если нужно
        return START_STICKY;
    }

    public void handleStopCommand() {
        //stopTimerInService();
        stopForeground(true); // это убирает уведомление у foreground‑сервиса
        //notificationManager.cancel(NOTIFICATION_ID); // явная отмена на всякий случай
        stopSelf();
    }

    private Notification buildNotification() {
        Intent stopIntent = new Intent(this, TimerControlReceiver.class);
        stopIntent.setAction(TimerControlReceiver.ACTION_STOP_TIMER);

        PendingIntent stopPending = PendingIntent.getBroadcast(
                this, 1001, stopIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Таймер")
                .setContentText("Время истекло!")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Стоп", stopPending)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "Звук таймера", NotificationManager.IMPORTANCE_HIGH);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopForeground(STOP_FOREGROUND_REMOVE);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}

