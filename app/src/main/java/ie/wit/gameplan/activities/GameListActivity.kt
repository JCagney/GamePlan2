package ie.wit.gameplan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.gameplan.R
import ie.wit.gameplan.adapters.GameAdapter
import ie.wit.gameplan.adapters.GameListener
import ie.wit.gameplan.databinding.ActivityGameListBinding
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.UserModel
import timber.log.Timber

class GameListActivity : AppCompatActivity(), GameListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGameListBinding

    var user = UserModel()

    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.extras?.getParcelable("user")!!
        Timber.i("Logged in: $user")

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadGames()

        registerRefreshCallback()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GameActivity::class.java)
                launcherIntent.putExtra("user", user)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGameClick(game: GameModel) {
        val launcherIntent = Intent(this, GameViewActivity::class.java)
        launcherIntent.putExtra("game_view", game)
        launcherIntent.putExtra("user", user)
        refreshIntentLauncher.launch(launcherIntent)
    }


    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadGames()}
    }

    private fun loadGames() {
        showGames(app.games.findAll())
    }

    fun showGames (games: List<GameModel>) {
        binding.recyclerView.adapter = GameAdapter(games, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}

