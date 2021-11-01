package kyr.company.customcalendarviewwithevents

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = intent.getStringExtra("event")
        val time = intent.getStringExtra("time")
        val notiID = intent.getIntExtra("id",0)
        val activityIntent : Intent = Intent(context,MainActivity::class.java)
        val pendingIntent : PendingIntent = PendingIntent.getActivity(context,0,activityIntent,PendingIntent.FLAG_ONE_SHOT)

        val channelId = "channel_id"
        val name:CharSequence ="channel_name"
        val description = "description"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,name,NotificationManager.IMPORTANCE_HIGH)
            channel.description = description
            val notificationManager : NotificationManager? = context.getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }

        val notification : Notification = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(event)
            .setContentText(time)
//            .setDeleteIntent(pendingIntent)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup("Group_calendar_view")
            .build()
        val notificationChannelCompat = NotificationManagerCompat.from(context)
        notificationChannelCompat.notify(notiID,notification)


    }
}