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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
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
            switch (intentAction){
                case Intent.ACTION_POWER_DISCONNECTED:
//                    Intent serviceIntent = new Intent();
//                    serviceIntent.putExtra("phone_number", "+79156954581");
//                    serviceIntent.putExtra("message", "Зарядка отключена!");
//                    SmsJobIntentService.enqueueWork(context, serviceIntent);
                    //sendReplySms(context, number[0], toastMessage);
                    //turnOnScreen();

                    if (Objects.equals(number[0], "number")){
                        System.out.println(number[0]);
                    }else {
                        if (Objects.equals(number[1], "on")){
                            System.out.println("ooooooonnnnn");
                            System.out.println(number[0]);
                            Intent serviceIntent = new Intent();
                            serviceIntent.putExtra("phone_number", "+79156954581");
                            serviceIntent.putExtra("message", "Зарядка отключена!");
                            SmsJobIntentService.enqueueWork(context, serviceIntent);
                            //scheduleSmsAfterUnplug(context, number[0], toastMessage);
                            //sendReplySms(context, number[0], toastMessage);
                        }
                    }

//                    PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
//                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
//                            "MyApp::MyWakelockTag"
//                    );
//                    wakeLock.acquire();
//                    Toast.makeText(context, "включен экран", Toast.LENGTH_LONG).show();



                    toastMessage = "Блок питания отключен! Устройство не заряжается! Возможно нет электричества. Отправляем СМС ";
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        context.startForegroundService(new Intent(context, RingtonePlayingService.class));
//                    } else {
//                        context.startService(new Intent(context, RingtonePlayingService.class));
//                    }
                    try {
                        Intent i = new Intent(context, RingtonePlayingService.class);
                        //context.startWakefulService(i);

                        context.startService(i);
                    }catch (RuntimeException e) {
                        System.out.println(e);
                        //Toast.makeText(context, (CharSequence) e, Toast.LENGTH_LONG).show();
                        Intent serviceIntent1 = new Intent(context, RingtonePlayingService.class);
                        context.startForegroundService(serviceIntent1 );
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            context.startForegroundService(serviceIntent);
//                        } else {
//                            context.startService(serviceIntent);
//                        }
                    }

                    if (Objects.equals(number[0], "number")){
                        System.out.println(number[0]);
                    }else {
                        if (Objects.equals(number[1], "on")){
                            System.out.println("ooooooonnnnn");
                            System.out.println(number[0]);

//                            Intent serviceIntent = new Intent(context, SmsService.class);
//                            serviceIntent.putExtra("phoneNumber", number[0]);
//                            serviceIntent.putExtra("message", toastMessage);
//                            context.startForegroundService(serviceIntent);
// Зарядка отключена — планируем отправку SMS через 30 секунд
                            scheduleSmsAfterUnplug(context, number[0], toastMessage);
                            //sendReplySms(context, number[0], toastMessage);
//                            Intent serviceIntent3 = new Intent();
//                            serviceIntent3.putExtra("phone_number", "+79156954581");
//                            serviceIntent3.putExtra("message", "Зарядка отключена3333!");
//                            SmsJobIntentService.enqueueWork(context, serviceIntent3);

//                            Intent serviceIntent = new Intent(context, SmsSendingService.class);
//                            serviceIntent.putExtra("phoneNumber", number[0]);
//                            serviceIntent.putExtra("message", toastMessage);
//                            context.startService(serviceIntent);

//                            try {
//                                // Get the default instance of the SmsManager
//                                // Создаём Intent с кастомным действием
//                                Intent sendSmsIntent = new Intent(ACTION_SEND_SMS);
//                                PendingIntent piSend = PendingIntent.getBroadcast(
//                                        context,                    // вместо getActivity()
//                                        0,                      // requestCode
//                                        sendSmsIntent,          // наш Intent
//                                        PendingIntent.FLAG_IMMUTABLE // флаг для совместимости с Android 12+
//                                );
//                                SmsManager smsManager = SmsManager.getDefault();
//                               // PendingIntent piSend = PendingIntent.getBroadcast(context, 0, new Intent(Manifest.permission.SEND_SMS), PendingIntent.FLAG_IMMUTABLE);
//                                // PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent(Manifest.SMS_DELIVERED), 0);
//
//                                smsManager.sendTextMessage(number[0],
//                                        null,
//                                        toastMessage,
//                                        piSend,
//                                        null);
//                                Toast.makeText(context, "СМС успешно отправлено!",
//                                        Toast.LENGTH_LONG).show();
//                            } catch (Exception ex) {
//                                Toast.makeText(context,"Your sms has failed...",
//                                        Toast.LENGTH_LONG).show();
//                                ex.printStackTrace();
//
//                            }
                        }

                    }

                    //Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(context, RingtonePlayingService.class);
//                    context.startService(i);

                    //context.startService(startIntent);
                    break;
                case Intent.ACTION_POWER_CONNECTED:
                    // Зарядка подключена — можно отменить запланированные задачи
                    WorkManager.getInstance(context).cancelAllWorkByTag("sms_after_unplug");
                    toastMessage = "Блок питания подключен! Устройство заряжается!";
//                    Intent i2= new Intent(context, RingtonePlayingService.class);
//                    context.stopService(i2);

                    break;



            }

            //Display the toast.
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "intentAction == null", Toast.LENGTH_LONG).show();
        }


    }
    private void scheduleSmsAfterUnplug(Context context, String phone_number, String  message) {
        Data inputData = new Data.Builder()
                .putString("phone_number", phone_number) // Замените на нужный номер
                .putString("message", message)
                .build();

        OneTimeWorkRequest smsWork = new OneTimeWorkRequest.Builder(SmsWorker.class)
                .setInputData(inputData)
                .setInitialDelay(5, TimeUnit.SECONDS) // Задержка 30 секунд
                .addTag("sms_after_unplug")
                .build();

        WorkManager.getInstance(context).enqueue(smsWork);
    }
    private void sendReplySms(Context context, String phoneNumber, String message) {
        try {
            Context appContext = context.getApplicationContext();
            SmsManager smsManager = SmsManager.getDefault();
            // Создаём PendingIntent для отслеживания статуса отправки
            String SENT = "SMS_SENT";
            PendingIntent sentPI = PendingIntent.getBroadcast(appContext, 0,
                    new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);

//            // Регистрируем BroadcastReceiver для обработки результата
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.registerReceiver(new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context arg0, Intent arg1) {
//                        switch (getResultCode()) {
//                            case Activity.RESULT_OK:
//                                Toast.makeText(context, "SMS отправлено", Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                                Toast.makeText(context, "Общая ошибка", Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NO_SERVICE:
//                                Toast.makeText(context, "Нет сети", Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NULL_PDU:
//                                Toast.makeText(context, "Ошибка PDU", Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_RADIO_OFF:
//                                Toast.makeText(context, "Радио выключено", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                }, new IntentFilter(SENT), Context.RECEIVER_NOT_EXPORTED);
//            }

            smsManager.sendTextMessage(phoneNumber, null, message, sentPI, null);
            Toast.makeText(appContext, "СМС успешно отправлено из sendReplySms!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка отправки SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
//        finally {
//            // Отменяем регистрацию BroadcastReceiver
//            try {
//                context.unregisterReceiver(receiver);
//            } catch (IllegalArgumentException e) {
//                // Receiver не был зарегистрирован
//            }
//        }
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

//    private  FileInputStream openFileInput(String file) {
//        String line1 = null;
//        String line2 = null;
//        StringBuilder sb = new StringBuilder();
//        try (FileInputStream fis = openFileInput(file);
//             InputStreamReader isr = new InputStreamReader(fis);
//             BufferedReader br = new BufferedReader(isr)) {
//
//            String line = br.readLine();
//            if (line != null) {
//                line1 = line;
//                line = br.readLine();
//                if (line != null) {
//                    line2 = line;
//                }
//            }
//
//
//            System.out.println(line1);
//            System.out.println(line2);
//
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

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