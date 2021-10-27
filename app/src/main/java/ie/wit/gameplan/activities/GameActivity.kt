package ie.wit.gameplan.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.ActivityGameBinding
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.Location
import ie.wit.gameplan.models.UserModel
import timber.log.Timber.i
import java.time.LocalDate

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    lateinit  var app : MainApp

    var game = GameModel()

    var user = UserModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.extras?.getParcelable("user")!!

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        var edit = false

        if (intent.hasExtra("game_edit")) {
            edit = true
            game = intent.extras?.getParcelable("game_edit")!!
            binding.gameTitle.setText(game.title)
            binding.description.setText(game.description)
            binding.btnAdd.setText(R.string.save_game)

        }

        binding.btnAdd.setOnClickListener() {
            game.title = binding.gameTitle.text.toString()
            game.description = binding.description.text.toString()
            if (game.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_game_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.games.update(game.copy())
                    val resultIntent = Intent()
                    resultIntent.putExtra("game", game)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()

                } else {
                    game.creator = "${user.firstName} ${user.lastName}"
                    app.games.create(game.copy())
                    setResult(RESULT_OK)
                    finish()
                }
            }

        }

        binding.gameLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (game.zoom != 0f) {
                location.lat =  game.lat
                location.lng = game.lng
                location.zoom = game.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val date = LocalDate.parse(game.date)
        datePicker.init(date.year, date.monthValue -1, date.dayOfMonth)
        {
                datePicker, year, month, day ->
            game.date = LocalDate.of(year, month + 1, day).toString()


        }

        registerMapCallback()


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

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            game.lat = location.lat
                            game.lng = location.lng
                            game.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}


