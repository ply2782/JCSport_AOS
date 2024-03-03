package com.jc.jcsports.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jc.jcsports.R;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Utils.logCheck("firebase update Token : " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Utils.logCheck(message.toString());
        // TODO(developer): Handle FCM messages here.
        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            Utils.logCheck("Message data payload: " + message.getData());
            Map<String, String> getData = message.getData();
            String myMessage = getData.get("message");
            createJoinNotification(myMessage);
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            Utils.logCheck("Message Notification Body: " + message.getNotification().getBody());
            String bodyMessage = message.getNotification().getBody();
            createJoinNotification(bodyMessage);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Utils.logCheck("onDeletedMessages");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.logCheck("onDestroy");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.logCheck("onCreate");
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Utils.logCheck("onLowMemory");
    }


    /**
     * 채팅 형식 노티피케이션
     */
    public void createJoinNotification(String message) {
        String channelId = "joinChannel_" + getPackageName();

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "JCSports", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel description");
            notificationChannel.setVibrationPattern(new long[]{0, 1000});
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        Bitmap jcLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.jc_logo);
        jcLogo = Utils.getBitmapCircleCrop(jcLogo, 0, 0);
        IconCompat userIcon = IconCompat.createWithBitmap(jcLogo);
        String userName = "JCSports";
        long timestamp = System.currentTimeMillis();
        Person user = new Person.Builder().setIcon(userIcon).setName(userName).build();
        NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle(user);
        style.addMessage(message, timestamp, user);
        builder.setLargeIcon(jcLogo)
                .setSmallIcon(R.mipmap.jc_logo)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_SOUND)
                .setVibrate(new long[]{1000})
                .setStyle(style)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        notificationManager.notify(ServerURL.joinNotification, builder.build());
    }
}
