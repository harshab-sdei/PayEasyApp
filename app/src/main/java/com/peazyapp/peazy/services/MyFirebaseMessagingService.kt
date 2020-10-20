package com.peazyapp.peazy.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.peazyapp.peazy.R
import com.peazyapp.peazy.controllers.SplashScreen
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseNotification"
    lateinit var notificationManager: NotificationManager
    private val ADMIN_CHANNEL_ID = "Android4Dev"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        UserPreferenc.setStringPreference(Constants.DEVICE_TOKEN, "" + token)

        Log.i(TAG, "MyFirebaseToken: " + token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {

        } catch (e: Exception) {
            Log.i(TAG, "Error: " + e.message)
        }
        Log.i(TAG, "from: ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            val title: String = remoteMessage.data.get("title").toString()
            val msg: String = remoteMessage.data.get("body").toString()
            sendNotification(title, msg)
        }

        remoteMessage.notification.let {
            val title: String = remoteMessage.notification?.title.toString()
            val msg: String = remoteMessage.notification!!.body.toString()
            sendNotification(title, msg)
        }

    }

    fun notification(title: String, msg: String) {

        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupNotificationChannels()
        }
        val notificationId = Random().nextInt(60000)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)  //a resource for your custom small icon
            .setContentTitle(title) //the "title" value you sent in your notification
            .setContentText(msg) //ditto
            .setAutoCancel(true)  //dismisses the notification on click
            .setSound(defaultSoundUri)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(
            notificationId /* ID of notification */,
            notificationBuilder.build()
        )


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels() {

        val adminChannelName = getString(R.string.notifications_admin_channel_name)
        val adminChannelDescription = getString(R.string.notifications_admin_channel_description)

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        adminChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        notificationManager.createNotificationChannel(adminChannel)
    }


    private fun sendNotification(title: String, msg: String) {
        try {

            var intent: Intent? = null

            intent = Intent(this, SplashScreen::class.java)

            intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
            )
            val channelId = getString(R.string.notifications_admin_channel_name)
            val defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationBuilder: NotificationCompat.Builder

            notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setColor(Color.rgb(93, 123, 136))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            } else {
                NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setVibrate(LongArray(0))
            }
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                setupNotificationChannels()
            }
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}