package com.rebliss.presenter.helper

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.rebliss.R
import com.rebliss.data.perf.MySingleton
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.notificationlist.Desc
import com.rebliss.notificationcenter.NotificationData
import com.rebliss.view.activity.ActivitySplash
import com.rebliss.view.activity.notification.NotificationListActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


    class FirebaseMessagingService : FirebaseMessagingService() {
        override fun onNewToken(s: String) {
            super.onNewToken(s)
            mySingleton = MySingleton(applicationContext)
            mySingleton!!.saveData(Constant.DEVICE_FCM_TOKEN, s)
            Log.e(TAG,
                "onNewToken: >>>>>>>>>>>>>>>>>>>>>>>>>$s")
        }

        var notificationCount = 0
        protected var mySingleton: MySingleton? = null
        private var notificationData: NotificationData? = null
        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            Log.e(TAG, "Message raw data payload: ")
            if (remoteMessage.data.size > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.data)
                val am = applicationContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
                val cn = am.getRunningTasks(1)[0].topActivity
                Log.e(TAG, " ***** class name***" + cn!!.className)
                val currentTopActivity: String
                currentTopActivity = cn.className
                Log.e(TAG, "From: " + remoteMessage.from)
                Log.e(TAG, "Message data payload: " + remoteMessage.data)
                if (remoteMessage.data.size > 0) {
                    Log.d(TAG, "Message data payload: " + remoteMessage.data)
                    if ( /* Check if data needs to be processed by long running job */true) {
                        // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                        notificationData = NotificationData()
                        try {
                            val jsonObject = JSONObject(remoteMessage.data.toString())
                            Log.i(TAG,
                                "Responce $jsonObject")
                            val gson = Gson()
                            notificationData = gson.fromJson(jsonObject.toString(),
                                NotificationData::class.java)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        //notificationData = (NotificationData)remoteMessage.getData();
                        val gson = Gson()
                        val Responce = gson.toJson(notificationData, NotificationData::class.java)
                        Log.i(TAG,
                            "Responce $Responce")
                        scheduleJob(notificationData!!)
                    }
                }
                if (remoteMessage.notification != null) {
                    Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!
                        .body)
                }
            } else {
                if (remoteMessage.notification != null) {
                    Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!
                        .body)
                }
            }
        }

        private fun scheduleJob(notificationData: NotificationData) {
            Log.d(TAG, "scheduleJob lived task is done.")
            val am = applicationContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val cn = am.getRunningTasks(1)[0].topActivity
            Log.e(TAG, " ***** class name***" + cn!!.className)
            val currentTopActivity: String
            currentTopActivity = cn.className
            sendNotificationDashBoard()
            if (notificationData.message.message.contains("Your profile has declined")) {
                doRestart(baseContext)
            }
        }

        private fun sendNotificationDashBoard() {
            val intent = Intent(this, ActivitySplash::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val resultIntent = Intent(this,
                NotificationListActivity::class.java)
            val desc = Desc()
            desc.id = notificationData!!.message.notificationId
            desc.pushMessage = notificationData!!.message.pushMessage
            desc.extraData = notificationData!!.message.extraData
            desc.created = notificationData!!.message.createdAt
            desc.imgUrl = notificationData!!.message.iageUr
            desc.link = notificationData!!.message.link
            resultIntent.putExtra("desc", desc)
            Log.e(TAG, "777777: " + notificationData!!.message.iageUr)
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            resultIntent.action = Intent.ACTION_MAIN
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val i = Intent("android.intent.action.MAIN")
            this.sendBroadcast(resultIntent)
            val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
            val uniqueInt = (System.currentTimeMillis() and 0xfffffff).toInt()
            val pendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val largeIcon = BitmapFactory.decodeResource(
                resources, R.drawable.ic_add_circle)
            val channelId = getString(R.string.default_notification_channel_id)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
           /* val bitmap = getBitmapFromURL(
                notificationData!!.message.iageUr)*/
            val urlImage:URL = URL(notificationData!!.message.iageUr)
            Log.e(TAG, "sendNotificationDashBoard: sf"+notificationData!!.message.iageUr )
            val bitmap = getBitmapFromURL(notificationData!!.message.iageUr)
            val result: Deferred<Bitmap?> = GlobalScope.async {
                urlImage.toBitmap()
            }

            GlobalScope.launch(Dispatchers.Main) {
                // show bitmap on image view when available

                Log.e(TAG, "sendNotificationDashBoard: "+result )
                val notificationBuilder: NotificationCompat.Builder
                notificationBuilder = NotificationCompat.Builder(this@FirebaseMessagingService, channelId)
                    .setSmallIcon(R.drawable.ic_icon_calender)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(notificationData!!.message.pushMessage)
                    .setContentText(notificationData!!.message.extraData)
                    .setAutoCancel(true)
                    .setLargeIcon(bitmap)
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH)
                    notificationManager.createNotificationChannel(channel)
                }
                notificationManager.notify(m, notificationBuilder.build())
            }

        }

        companion object {
            private const val TAG = "MyFirebaseMsgService"
            fun doRestart(c: Context?) {
                try {
                    //check if the context is given
                    if (c != null) {
                        val pm = c.packageManager
                        //check if we got the PackageManager
                        if (pm != null) {
                            //create the intent with the default start activity for your application
                            val mStartActivity = pm.getLaunchIntentForPackage(
                                c.packageName
                            )
                            if (mStartActivity != null) {
                                mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                val mPendingIntentId = 223344
                                val mPendingIntent = PendingIntent
                                    .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT)
                                val mgr = c.getSystemService(ALARM_SERVICE) as AlarmManager
                                mgr[AlarmManager.RTC, System.currentTimeMillis() + 100] =
                                    mPendingIntent
                                System.exit(0)
                            } else {
                                Log.e(TAG,
                                    "Was not able to restart application, mStartActivity null")
                            }
                        } else {
                            Log.e(TAG, "Was not able to restart application, PM null")
                        }
                    } else {
                        Log.e(TAG, "Was not able to restart application, Context null")
                    }
                } catch (ex: Exception) {
                    Log.e(TAG, "Was not able to restart application")
                }
            }

            const val NOTIFICATION_CHANNEL_ID = "10001"
            private const val default_notification_channel_id = "default"
            private val c = AtomicInteger(0)
            val iD: Int
                get() = c.incrementAndGet()

            fun getBitmapFromURL(src: String?): Bitmap? {
                return try {
                    val url = URL(src)
                    val connection =
                        url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val input = connection.inputStream
                    BitmapFactory.decodeStream(input)
                } catch (e: IOException) {
                    null
                }
            }
        }

        private fun getBitmapFromURL(src: String?):Bitmap? {
            var bmp : Bitmap ? = null
            CoroutineScope(Job() + Dispatchers.IO).launch {
                try {
                    val url = URL(src)
                    val bitMap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    bmp = Bitmap.createScaledBitmap(bitMap, 100, 100, true)
                } catch (e: IOException) {
                }
            }
            return bmp
        }
        fun URL.toBitmap(): Bitmap?{
            return try {
                BitmapFactory.decodeStream(openStream())
            }catch (e:IOException){
                null
            }
        }

}