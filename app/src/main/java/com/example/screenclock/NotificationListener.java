package com.example.screenclock;

import android.os.Binder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationListener extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        getActiveNotifications();
        // Обработка нового уведомления
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Обработка удалённого уведомления
    }
}
