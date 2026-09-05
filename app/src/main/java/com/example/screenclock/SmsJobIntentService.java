package com.example.screenclock;

import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class SmsJobIntentService extends JobIntentService {
    private static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SmsJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String phoneNumber = intent.getStringExtra("phone_number");
        String message = intent.getStringExtra("message");

        if (phoneNumber != null && message != null) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                System.out.println("sms was sanded");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
