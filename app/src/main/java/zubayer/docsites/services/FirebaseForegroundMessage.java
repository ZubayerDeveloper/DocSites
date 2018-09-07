package zubayer.docsites.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Random;

import zubayer.docsites.R;
import zubayer.docsites.activity.Reply;

public class FirebaseForegroundMessage extends FirebaseMessagingService {
    public static final String TAG = "Mytag";
    Intent intent;
    PendingIntent pendingIntent;
    HashMap<String,String> dataPlayLoad=new HashMap<>();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            dataPlayLoad.putAll(remoteMessage.getData());
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                notification("firebase", "firebase_channel", remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), notifyID());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message,
    }

    private int notifyID() {
        Random random=new Random();
        random.nextInt();
        return random.nextInt();
    }

    private void notification(String channel_id, String channel_name, String title, String text, int notify_id) {
        intent=new Intent(this, Reply.class);
        intent.putExtra("postID",dataPlayLoad.get("postID"));
        intent.putExtra("intent",dataPlayLoad.get("intent"));
        pendingIntent = PendingIntent.getActivity(this, notifyID(), intent, 0);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_HIGH);
            channel.shouldShowLights();
            channel.shouldVibrate();
            channel.canShowBadge();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            Notification notification = new NotificationCompat.Builder(FirebaseForegroundMessage.this)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setColor(0xff990000)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setVibrate(new long[]{0, 300, 300, 300})
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                    .setChannelId(channel_id).build();
            notification.ledARGB = 0xff990000;
            notification.ledOnMS = 500;
            notification.ledOffMS = 100;

            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = null;
            if (pm != null) {
                wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
            }
            if (wakeLock != null) {
                wakeLock.acquire(100);
            }
            notificationManager.notify(notify_id, notification);
        } else {
            notification2(title, text, notify_id);
        }
    }

    private void notification2(String title, String text, int id) {
        intent=new Intent(this, Reply.class);
        intent.putExtra("postID",dataPlayLoad.get("postID"));
        intent.putExtra("intent",dataPlayLoad.get("intent"));
        pendingIntent = PendingIntent.getActivity(this, notifyID(), intent, 0);
        NotificationCompat.BigTextStyle bigTextStyle;
        bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(text);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setColor(0xff990000)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{0, 300, 300, 300})
                .setLights(Color.GREEN, 1000, 1000)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(bigTextStyle)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground));
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = null;
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        }
        if (wakeLock != null) {
            wakeLock.acquire(100);
        }
        notificationManager.notify(id, notificationBuilder.build());
    }
}
