package ie.wit.gameplan.main

import android.app.Application
import ie.wit.gameplan.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    //lateinit var games: GameStore


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //games = GameJSONStore(applicationContext)
        //users = UserJSONStore(applicationContext)
        i("GamePlan started")
    }
}