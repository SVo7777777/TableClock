package com.example.screenclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SmsWorker extends Worker {

    public SmsWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        String phoneNumber = getInputData().getString("phone_number");
        String message = getInputData().getString("message");

        if (phoneNumber == null || message == null) {
            return Result.failure();
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            showNotification("SMS отправлено на " + phoneNumber);
            System.out.println("СМС из Worker успешно отправлено!");
            return Result.success();
        } catch (SecurityException e) {
            showNotification("Ошибка: нет разрешения на отправку SMS");
            e.printStackTrace();
            return Result.failure();
        } catch (IllegalArgumentException e) {
            showNotification("Ошибка: некорректный номер телефона");
            e.printStackTrace();
            return Result.failure();
        } catch (Exception e) {
            showNotification("Общая ошибка отправки SMS: " + e.getMessage());
            e.printStackTrace();
            return Result.retry();
        }
    }
    @SuppressLint("MissingPermission")
    private void showNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "sms_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Отправка SMS")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, builder.build());
    }
}

