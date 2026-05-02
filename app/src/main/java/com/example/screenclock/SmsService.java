package com.example.screenclock;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsService extends Service {
//    static Ringtone r;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            System.out.println("Служба СМС запущена");
        // Get the default instance of the SmsManager
            String phoneNumber = intent.getStringExtra("phoneNumber");
            String message = intent.getStringExtra("message");
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent piSend = PendingIntent.getBroadcast(getBaseContext(), 0, new Intent(android.Manifest.permission.SEND_SMS), PendingIntent.FLAG_IMMUTABLE);
            // PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent(Manifest.SMS_DELIVERED), 0);

            smsManager.sendTextMessage(phoneNumber,
                    null,
                    message,
                    piSend,
                    null);
            Toast.makeText(getBaseContext(), "СМС из службы успешно отправлено!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(),"Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();

        }
//        System.out.println("play");

        return START_NOT_STICKY;
    }
//    @SuppressLint("ForegroundServiceType")
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        int NOTIFICATION_ID = (int) (System.currentTimeMillis()%10000);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(NOTIFICATION_ID, new Notification.Builder(this).build());
//        }
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (notification == null) {
//            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//        r = RingtoneManager.getRingtone(getBaseContext(), notification);
//        //playing sound alarm
//        r.play();
//        System.out.println("play");
//
//
//        // Do whatever you want to do here
//    }


    @Override
    public void onDestroy()
    {
//        r.stop();

        System.out.println("stop");
    }
}
