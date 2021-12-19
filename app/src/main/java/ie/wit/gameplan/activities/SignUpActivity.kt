package ie.wit.gameplan.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.ActivitySignUpBinding
import ie.wit.gameplan.helpers.showImagePicker
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.UserModel
import ie.wit.gameplan.ui.auth.Login
import timber.log.Timber
import timber.log.Timber.i

class SignUpActivity : AppCompatActivity() {

    lateinit var app: MainApp

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var returnIntentLauncher: ActivityResultLauncher<Intent>

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    //create an empty uri for the optional profile pic
    var image: Uri = Uri.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MainApp

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.title = title
        setSupportActionBar(binding.signUp)

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.btnLogin.setOnClickListener() {
            var email = binding.email.text.toString().lowercase()
            Timber.i("Email: $email")

            //check if the email is already registered
            var user: UserModel? = app.users.findAll().firstOrNull { it.email == email }
            if (user == null) {
                var firstName = binding.firstName.text.toString()
                var secondName = binding.secondName.text.toString()
                //store a basic hash of the password
                var passwordHash = binding.password.text.toString().hashCode()
                //creeate the user and return to the login activity
                app.users.create(UserModel(email, firstName, secondName, passwordHash))
                val launcherIntent = Intent(this, Login::class.java)
                returnIntentLauncher.launch(launcherIntent)
            }
            else
            {
                Snackbar.make(it, R.string.email_taken, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        registerReturnCallback()
        registerImagePickerCallback()


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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            image = result.data!!.data!!
                            Picasso.get()
                                .load(image)
                                .into(binding.profilePic)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}