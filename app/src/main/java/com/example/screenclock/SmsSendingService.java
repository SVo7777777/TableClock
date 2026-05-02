package com.example.screenclock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

public class SmsSendingService extends IntentService {

    private static final String CHANNEL_ID = "sms_service_channel";


    public SmsSendingService() {
        super("SmsSendingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String phoneNumber = intent.getStringExtra("phoneNumber");
            String message = intent.getStringExtra("message");

            if (phoneNumber != null && message != null) {
                sendSms(phoneNumber, message);
            }
        }
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();

            // Создаём PendingIntent для отслеживания статуса отправки
            String SENT = "SMS_SENT";
            Intent sentIntent = new Intent(SENT);
            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent, PendingIntent.FLAG_IMMUTABLE);

//            // Регистрируем BroadcastReceiver для обработки результата
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                registerReceiver(new android.content.BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        switch (getResultCode()) {
//                            case Activity.RESULT_OK:
//                                showNotification("SMS отправлено успешно на номер: " + phoneNumber);
//                                break;
//                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                                showNotification("Ошибка: общая ошибка отправки SMS");
//                                break;
//                            case SmsManager.RESULT_ERROR_NO_SERVICE:
//                                showNotification("Ошибка: нет сети для отправки SMS");
//                                break;
//                            case SmsManager.RESULT_ERROR_NULL_PDU:
//                                showNotification("Ошибка: некорректный PDU");
//                                break;
//                            case SmsManager.RESULT_ERROR_RADIO_OFF:
//                                showNotification("Ошибка: радиомодуль выключен");
//                                break;
//                            default:
//                                showNotification("Ошибка отправки SMS: код " + getResultCode());
//                                break;
//                        }
//                    }
//                }, new IntentFilter(SENT), Context.RECEIVER_NOT_EXPORTED);
//            }

            // Проверяем длину сообщения
            if (message.length() > 160) {
                // Разбиваем длинное сообщение на части
                ArrayList<String> parts = smsManager.divideMessage(message);
                smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
                showNotification("Успешно отправлено SMS");
            } else {
                smsManager.sendTextMessage(phoneNumber, null, message, sentPI, null);
            }

        } catch (SecurityException e) {
            showNotification("Ошибка: нет разрешения на отправку SMS");
            e.printStackTrace();
        } catch (Exception e) {
            showNotification("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
//        } finally {
//            // Отменяем регистрацию BroadcastReceiver
//            try {
//                unregisterReceiver(receiver);
//            } catch (IllegalArgumentException e) {
//                // Receiver не был зарегистрирован
//            }
        }
    }

    @SuppressLint("MissingPermission")
    private void showNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("SMS Service")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}

