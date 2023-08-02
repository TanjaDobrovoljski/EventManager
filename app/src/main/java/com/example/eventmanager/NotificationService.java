package com.example.eventmanager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationService extends Service {

    private static final long CHECK_INTERVAL = 60 * 1000; // 1 minute interval

    private DBHelper dbHelper;
    private Handler handler;
    private Runnable notificationRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(this);
        handler = new Handler();
        notificationRunnable = () -> {
            checkForUpcomingNotifications();
            handler.postDelayed(notificationRunnable, CHECK_INTERVAL);
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startNotificationTask();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopNotificationTask();
    }

    private void startNotificationTask() {
        handler.post(notificationRunnable);
    }

    private void stopNotificationTask() {
        handler.removeCallbacks(notificationRunnable);
    }

    private void checkForUpcomingNotifications() {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Get the user's notification preference
        int selectedPosition = getNotificationSelectedPosition();

        if (selectedPosition > 0) {
            // Get the number of days before the event for notification
            int daysBefore = getDaysBefore(selectedPosition);

            // Calculate the date for notification based on the preference
            Calendar notificationDate = (Calendar) currentDate.clone();
            notificationDate.add(Calendar.DAY_OF_MONTH, daysBefore);

            // Retrieve activities that match the notification criteria
            List<Activity> activities = dbHelper.getActivitiesForDateRange(
                    formatDate(currentDate),
                    Integer.parseInt(formatDate(notificationDate))
            );

            // Send a broadcast to update the upcoming notifications
            Intent intent = new Intent("com.example.eventmanager.UPCOMING_NOTIFICATIONS");
            intent.putExtra("activities", new ArrayList<>(activities));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private int getNotificationSelectedPosition() {
        SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
        return preferences.getInt("selectedPosition", 0);
    }

    private int getDaysBefore(int selectedPosition) {
        switch (selectedPosition) {
            case 1: // "1 hour before"
                return 1;
            case 2: // "1 day before"
                return 1;
            case 3: // "7 days before"
                return 7;
            default:
                return 0;
        }
    }

    private String formatDate(Calendar date) {
        return String.format("%04d-%02d-%02d", date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
