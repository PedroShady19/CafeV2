package com.productions.esaf.cafe.Helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.productions.esaf.cafe.R;

public class NotificationHelper extends ContextWrapper {

    private static final String CAFE_CHANEL_ID="com.productions.esaf.cafe";
    private static final String CAFE_CHANEL_NAME="CafeV2";
    private NotificationManager manager;
    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            createChannel();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel cafeChannel = new NotificationChannel(CAFE_CHANEL_ID,
                CAFE_CHANEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        cafeChannel.enableLights(false);
        cafeChannel.enableVibration(true);
        cafeChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(cafeChannel);
    }

    public NotificationManager getManager() {
        if(manager==null)
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getCafeV2ChannelNotification(String title, String body, PendingIntent contentIntent,
                                                             Uri soundUri)
    {
        return new android.app.Notification.Builder(getApplicationContext(),CAFE_CHANEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(false);
    }
}
