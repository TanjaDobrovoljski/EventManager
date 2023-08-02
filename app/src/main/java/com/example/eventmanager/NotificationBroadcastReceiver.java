package com.example.eventmanager;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_CHANNEL_ID = "event_manager_channel";

    private final Context context;

    public NotificationBroadcastReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle the notification broadcast
        ArrayList<Activity> activities = intent.getParcelableArrayListExtra("activities");
        if (activities != null) {
            showNotification(activities);
        }
    }

    private void showNotification(ArrayList<Activity> list) {
        if (list != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int i = 1;
            for (Activity a : list) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Event Manager")
                        .setContentText(a.getName() + " on: " + a.getDate() + " in " + a.getCity())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // Show the notification
                notificationManager.notify(i, builder.build());
                i++;
            }
        }
    }
}
