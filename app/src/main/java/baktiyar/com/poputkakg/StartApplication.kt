package baktiyar.com.poputkakg

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import baktiyar.com.poputkakg.ui.main.MainActivity
import baktiyar.com.poputkakg.util.ForumService
import baktiyar.com.poputkakg.util.Network

class StartApplication : Application() {
    lateinit var service: ForumService
    lateinit var notification:NotificationCompat.Builder

    companion object {
        lateinit var INSTANCE: StartApplication
    }


    override fun onCreate() {
        super.onCreate()
        INSTANCE = applicationContext as StartApplication
        service = Network.initService()

        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)

        notification = NotificationCompat.Builder(this,getString(R.string.channel_name))
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("PoputkaKG")
                .setContentText("Пользователь предлогает сделку")
                .setStyle(NotificationCompat.BigTextStyle().bigText("" +
                        "Слишком большой текст"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)


    }
}