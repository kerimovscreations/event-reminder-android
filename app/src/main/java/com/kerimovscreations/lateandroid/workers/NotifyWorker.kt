package com.kerimovscreations.lateandroid.workers

import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.kerimovscreations.lateandroid.R
import com.kerimovscreations.lateandroid.activities.MainActivity

class NotifyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        triggerNotification()
        return Result.success()
    }

    private fun triggerNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val eventId = inputData.getInt("EVENT_ID", 1)
        val soundId = inputData.getInt("SOUND_ID", R.raw.en_male_mins_0_left)
        val sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + soundId)
        val title = inputData.getString("TITLE")
        val mBuilder = NotificationCompat.Builder(applicationContext, "B")
                .setSmallIcon(R.drawable.ic_stat_icon)
                .setSound(null)
                .setContentTitle("LATE")
                .setContentText(title)
                .setVibrate(longArrayOf(0, 250, 250, 250))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        try {
            val r = RingtoneManager.getRingtone(applicationContext, sound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        notificationManager.notify(eventId, mBuilder.build())
    }
}