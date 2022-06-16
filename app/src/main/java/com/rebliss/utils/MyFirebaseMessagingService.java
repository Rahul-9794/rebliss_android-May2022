package com.rebliss.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.notificationlist.Desc;
import com.rebliss.notificationcenter.NotificationData;
import com.rebliss.view.activity.ActivitySplash;
import com.rebliss.view.activity.notification.NotificationListActivity;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        mySingleton=new MySingleton(getApplicationContext());
        mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, s);
        Log.e(TAG, "onNewToken: >>>>>>>>>>>>>>>>>>>>>>>>>"+s );
    }

    private static final String TAG = "MyFirebaseMsgService";

    protected MySingleton mySingleton;
    private NotificationData notificationData;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Message raw data payload: ");
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            Log.e(TAG, " ***** class name***" + cn.getClassName());
            String currentTopActivity;
            currentTopActivity = cn.getClassName();
            Log.e(TAG, "From: " + remoteMessage.getFrom());
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                if (/* Check if data needs to be processed by long running job */ true) {
                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                    notificationData = new NotificationData();
                    try {
                        JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                        Log.i(TAG, "Responce " + jsonObject);
                        Gson gson = new Gson();
                        notificationData = gson.fromJson(jsonObject.toString(), NotificationData.class);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //notificationData = (NotificationData)remoteMessage.getData();

                    Gson gson = new Gson();
                    String Responce = gson.toJson(notificationData, NotificationData.class);

                    Log.i(TAG, "Responce " + Responce);

                    scheduleJob(notificationData);
                } else {
                    // Handle message within 10 seconds
                    // handleNow(remoteMessage.getData().get("BidId"));
                }

            }

            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        } else {
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }
    }


    private void scheduleJob(NotificationData notificationData) {
        Log.d(TAG, "scheduleJob lived task is done.");
        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.e(TAG, " ***** class name***" + cn.getClassName());
        String currentTopActivity;
        currentTopActivity = cn.getClassName();
        sendNotificationDashBoard();
        if (notificationData.getMessage().getMessage().contains("Your profile has declined")) {
            doRestart(getBaseContext());
        }
    }

    private void restartApp() {
        try {
            Intent i = getBaseContext().getPackageManager().
                    getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC,System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }
    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(String bidId) {
        Log.d(TAG, "Short lived task is done.");
        // sendNotification(id);
    }
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    private void sendNotificationDashBoard() {

        Intent intent = new Intent(this, ActivitySplash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent resultIntent = new Intent(this, NotificationListActivity.class);

        Desc desc = new Desc();

        desc.setId((notificationData.getMessage().getNotificationId()));

        desc.setPushMessage(notificationData.getMessage().getPushMessage());
        desc.setExtraData(notificationData.getMessage().getExtraData());
        desc.setCreated(notificationData.getMessage().getCreatedAt());
        desc.setImgUrl(notificationData.getMessage().getIageUr());
        desc.setLink(notificationData.getMessage().getLink());
        resultIntent.putExtra("desc", desc);
        Log.e(TAG, "777777: " + notificationData.getMessage().getIageUr());

        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Intent i = new Intent("android.intent.action.MAIN");
        this.sendBroadcast(resultIntent);


        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap bitmap = getBitmapFromURL(notificationData.getMessage().getIageUr());
        if (bitmap != null) {
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.app_icon)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(notificationData.getMessage().getPushMessage())
                    .setContentText(notificationData.getMessage().getExtraData())
                    .setAutoCancel(true)
                    .setLargeIcon(bitmap)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(m, notificationBuilder.build());
        }

    }
    private final static AtomicInteger c = new AtomicInteger(0);

    public static int getID() {
        return c.incrementAndGet();
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }

}