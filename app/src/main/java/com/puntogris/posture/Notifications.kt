package com.puntogris.posture

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.puntogris.posture.data.local.LocalDataSource
import com.puntogris.posture.model.FcmNotification
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.ui.main.MainActivity
import com.puntogris.posture.utils.Constants.CLAIM_NOTIFICATION_EXP_INTENT
import com.puntogris.posture.utils.Constants.FMC_CHANNEL_ID
import com.puntogris.posture.utils.Constants.NAVIGATION_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Notifications @Inject constructor(@ApplicationContext private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val deprecatedChannels = listOf(
        "postureNotification"
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannelForReminderSdkO(reminder: Reminder){

        NotificationChannel(reminder.reminderId, context.getString(R.string.alarm_channel_name), NotificationManager.IMPORTANCE_DEFAULT).also {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            it.description = reminder.name
            it.enableVibration(true)
            it.enableLights(true)
            it.lightColor = Color.MAGENTA
            it.setSound(Uri.parse(reminder.soundUri), audioAttributes)

            if (reminder.soundUri.isNotBlank()) {
                it.setSound(Uri.parse(reminder.soundUri), audioAttributes )
            }
            if (reminder.vibrationPattern != 0) {
                it.vibrationPattern = LocalDataSource().vibrationPatterns[reminder.vibrationPattern]
            }

            notificationManager.createNotificationChannel(it)
        }
    }

    fun getNotificationBuilderWithReminder(reminder: Reminder): NotificationCompat.Builder{

        val intent =  Intent(context, MainActivity::class.java).apply {
            putExtra(NAVIGATION_DATA, CLAIM_NOTIFICATION_EXP_INTENT)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, reminder.reminderId)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(context.getString(R.string.posture_notification_title))
            .setContentText(context.getString(R.string.posture_notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_baseline_add_24, context.getString(R.string.claim_exp_notification_action_title), pendingIntent)
            .setAutoCancel(true)

        if (reminder.soundUri.isNotBlank()) {
            builder.setSound(Uri.parse(reminder.soundUri))
        }

        if (reminder.vibrationPattern != 0) {
            builder.setVibrate(LocalDataSource().vibrationPatterns[reminder.vibrationPattern])
        }

        return builder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun removeDeprecatedChannels(){
        deprecatedChannels.forEach(notificationManager::deleteNotificationChannel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun removeNotificationChannelWithId(channelId: String){
        notificationManager.deleteNotificationChannel(channelId)
    }

    private fun buildNotificationForFcm(fcmNotification: FcmNotification): Notification {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(fcmNotification.uriString)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, FMC_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(fcmNotification.title)
            .setContentText(fcmNotification.message)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .build()
    }

    fun sendFcmNotification(fcmNotification: FcmNotification){
        val notification = buildNotificationForFcm(fcmNotification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notification.channelId, 
                context.getString(R.string.fmc_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notification)
    }
}