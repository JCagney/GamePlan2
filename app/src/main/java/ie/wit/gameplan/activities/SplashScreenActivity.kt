package ie.wit.gameplan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ie.wit.gameplan.R
import ie.wit.gameplan.ui.auth.Login

class SplashScreenActivity : AppCompatActivity() {

    private val splashTimeOut:Long = 4000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(Intent(this, Login::class.java))
            finish()
        }, splashTimeOut)
    }
}