package zubayer.docsites;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseForegroundMessage extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("firebase", "firebaseNotification", NotificationManager.IMPORTANCE_HIGH);
            channel.shouldShowLights();
            channel.shouldVibrate();
            channel.canShowBadge();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            Notification notification = new NotificationCompat.Builder(FirebaseForegroundMessage.this)
                    .setContentTitle("Message from Dr. Zubayer")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setColor(0xff990000)
                    .setWhen(System.currentTimeMillis())
                    .setVibrate(new long[]{0, 300, 300, 300})
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                    .setChannelId("firebase").build();
            notification.ledARGB = 0xff990000;
            notification.ledOnMS = 500;
            notification.ledOffMS = 100;

            notificationManager.notify(012, notification);
        }else {
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(remoteMessage.getData().get("title"));
            bigTextStyle.bigText(remoteMessage.getData().get("message"));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setColor(0xff990000)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX)
                    .setLights(0xff990000, 300, 100)
                    .setStyle(bigTextStyle)
                    .setSmallIcon(R.mipmap.ic_launcher);

            notificationManager.notify(013, notificationBuilder.build());
        }
        }
}
