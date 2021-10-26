package ie.wit.gameplan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.ActivityGameBinding
import ie.wit.gameplan.databinding.ActivityLoginBinding
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.*
import timber.log.Timber.i

class LoginActivity : AppCompatActivity() {

    lateinit var app: MainApp

    //lateinit var user: UserModel

    private lateinit var binding: ActivityLoginBinding

    private lateinit var gameIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MainApp

        if (app.users.findAll().firstOrNull { it.email == "john@gameplan.ie" } == null)
            app.users.create(UserModel("john@gameplan.ie", "", "", ""))

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        binding.btnLogin.setOnClickListener() {
            var email = binding.email.text.toString()
            i("Email: $email")

            var user: UserModel? = app.users.findAll().firstOrNull { it.email == email }
            if (user != null) {
                val launcherIntent = Intent(this, GameListActivity::class.java)
                gameIntentLauncher.launch(launcherIntent)
            } else {
                Snackbar.make(it, R.string.login_failed, Snackbar.LENGTH_LONG)
                    .show()
            }

        }
        registerRefreshCallback()


    }

    private fun registerRefreshCallback() {
        gameIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}