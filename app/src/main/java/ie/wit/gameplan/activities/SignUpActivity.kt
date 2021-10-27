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
import ie.wit.gameplan.databinding.ActivityLoginBinding
import ie.wit.gameplan.databinding.ActivitySignUpBinding
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.UserModel
import timber.log.Timber

class SignUpActivity : AppCompatActivity() {

    lateinit var app: MainApp

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var returnIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MainApp

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.title = title
        setSupportActionBar(binding.signUp)

        binding.btnLogin.setOnClickListener() {
            var email = binding.email.text.toString()
            Timber.i("Email: $email")

            var user: UserModel? = app.users.findAll().firstOrNull { it.email == email }
            if (user == null) {
                var firstName = binding.firstName.text.toString()
                var secondName = binding.secondName.text.toString()
                var password = binding.password.text.toString()

                app.users.create(UserModel(email, firstName, secondName, password))
                val launcherIntent = Intent(this, LoginActivity::class.java)
                returnIntentLauncher.launch(launcherIntent)
            }
            else
            {
                Snackbar.make(it, R.string.email_taken, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        registerReturnCallback()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_game, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerReturnCallback() {
        returnIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}