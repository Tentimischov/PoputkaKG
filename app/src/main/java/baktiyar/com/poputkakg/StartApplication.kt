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





    }
}