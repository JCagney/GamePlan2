package ie.wit.gameplan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    private lateinit var binding: ActivityLoginBinding

    private lateinit var gameIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var signUpIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MainApp

        //create a user if it doesn't already exist (remove in prod))
        if (app.users.findAll().firstOrNull { it.email == "john@gameplan.ie" } == null)
            app.users.create(UserModel("john@gameplan.ie", "John", "Cagney", "password".hashCode() ))

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signIn.title = title
        setSupportActionBar(binding.signIn)


        binding.btnLogin.setOnClickListener() {
            var email = binding.email.text.toString().lowercase()
            i("Email: $email")

            //check if this user exists, if so check the password
            var user: UserModel? = app.users.findAll().firstOrNull { it.email == email }
            if (user != null) {
                if (binding.password.text.toString().hashCode() == user.passwordHash)
                {
                    val launcherIntent = Intent(this, Home::class.java)
                    launcherIntent.putExtra("user", user)
                    gameIntentLauncher.launch(launcherIntent)
                }
                else
                {
                    Snackbar.make(it, R.string.invalid_password, Snackbar.LENGTH_LONG)
                        .show()
                }
            } else {
                Snackbar.make(it, R.string.invalid_username, Snackbar.LENGTH_LONG)
                    .show()
            }

        }
        registerGameCallback()
        registerSignUpCallback()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_signin, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_signUp -> {
                val launcherIntent = Intent(this, SignUpActivity::class.java)
                signUpIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerGameCallback() {
        gameIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            //clear the password on return
            {binding.password.setText("") }
    }

    private fun registerSignUpCallback() {
        signUpIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {binding.password.setText("") }
    }
}