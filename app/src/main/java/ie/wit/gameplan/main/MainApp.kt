package ie.wit.gameplan.main

import android.app.Application
import ie.wit.gameplan.models.GameMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val games = GameMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("GamePlan started")
    }
}