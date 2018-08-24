package baktiyar.com.poputkakg

import android.app.Application
import baktiyar.com.poputkakg.util.ForumService
import baktiyar.com.poputkakg.util.Network

class StartApplication : Application() {
    lateinit var service: ForumService

    companion object {
        lateinit var INSTANCE: StartApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = applicationContext as StartApplication
        service = Network.initService()
    }
}