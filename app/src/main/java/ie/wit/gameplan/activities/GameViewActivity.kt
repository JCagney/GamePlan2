package ie.wit.gameplan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.ActivityGameBinding
import ie.wit.gameplan.databinding.ActivityGameViewBinding
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.Location
import timber.log.Timber

class GameViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameViewBinding
    var game = GameModel()
    lateinit var app: MainApp
    private lateinit var editIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        game = intent.extras?.getParcelable("game_view")!!
        Timber.i("Viewing game $game")
        binding.gameTitle.setText(game.title)
        binding.description.setText(game.description)

        binding.editGame.setOnClickListener {
            val launcherIntent = Intent(this, GameActivity::class.java)
                .putExtra("game_edit", game)
            editIntentLauncher.launch(launcherIntent)
        }

        binding.deleteGame.setOnClickListener {

            finish()
        }

        registerEditCallback()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_view_game, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_list -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerEditCallback() {
        editIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Received edit")
                            val game =
                                result.data!!.extras?.getParcelable<GameModel>("game")!!
                            binding.gameTitle.setText(game.title)
                            binding.description.setText(game.description)

                        }
                    }


                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }

}