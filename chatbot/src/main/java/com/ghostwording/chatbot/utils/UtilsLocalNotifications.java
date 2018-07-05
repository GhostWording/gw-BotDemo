package com.ghostwording.chatbot.utils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.model.NotificationsModel;

import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilsLocalNotifications {

    public static void showNotification(Context context, String message) {
        showNotification(context, message, null);
    }

    public static void showNotification(Context context, String message, String textId) {
        //Intent contentIntent = new Intent(context, MainActivity.class);
        //showNotification(context, contentIntent, message);
    }

    private static void showNotification(Context context, Intent contentIntent, String message) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(contentIntent);
        PendingIntent pending = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nfc = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(context.getString(R.string.app_name))
                .setContentTitle(message)
                .setAutoCancel(true)
                .setContentIntent(pending);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, nfc.build());
    }

    public static void setupAlarmByTime(Context context, long time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, broadcast);
    }

    public static long getNextNotificationTime() {
        Calendar calendar = Calendar.getInstance();
        int notificationFrequency = PrefManager.instance().getNotificationFrequency();
        switch (notificationFrequency) {
            case 0: {
                calendar.add(Calendar.DATE, 1);
                break;
            }
            case 1: {
                calendar.add(Calendar.DATE, 2);
                break;
            }
            case 2: {
                calendar.add(Calendar.DATE, 7);
                break;
            }
        }
        //random time between 8ap and 8pm
        calendar.set(Calendar.HOUR_OF_DAY, 8 + new Random().nextInt(12));
        calendar.set(Calendar.MINUTE, new Random().nextInt(60));
        Logger.i("Next alarm time: " + calendar.getTime().toString());
        return calendar.getTimeInMillis();
    }

    public static void loadNotifications() {
        ApiClient.getInstance().configService.getNotifications().enqueue(new Callback<NotificationsModel>() {
            @Override
            public void onResponse(Call<NotificationsModel> call, Response<NotificationsModel> response) {
                if (response.isSuccessful()) {
                    PrefManager.instance().saveNotifications(response.body());
                }
            }

            @Override
            public void onFailure(Call<NotificationsModel> call, Throwable t) {

            }
        });
    }

}

