package baktiyar.com.poputkakg.ui.notificationUtils

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.ui.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService :FirebaseMessagingService() {
    val TAG = "_____"
    override fun onMessageReceived(remoteMessage: RemoteMessage?){
        val CHANNEL_ID  = "CHANNEL_FOR_OREO "
        Log.d(TAG,"From: "+remoteMessage!!.from!!)

        if(remoteMessage.data.isNotEmpty()){
            Log.d(TAG,"Message data payload: "+remoteMessage.data)
            if(true){

            }else{
                val intent = Intent(this,RemoteMessage.Notification::class.java)
                startActivity(intent)
            }
        }
        if(remoteMessage.notification != null){
            Log.d(TAG,"Message notification body: "+remoteMessage.notification!!.body!!)
        }

        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent  = PendingIntent.getActivity(this,0,intent,0)

        val mBuilder = NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.app_name)
            val description = "У вас новое предложение"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID,name,importance)
            channel.description = description
            val manager  = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as  NotificationManager
            manager.createNotificationChannel(channel)
        }
        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(1,mBuilder.build())
    }
}