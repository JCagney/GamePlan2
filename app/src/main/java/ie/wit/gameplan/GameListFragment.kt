package ie.wit.gameplan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.gameplan.activities.GameViewActivity
import ie.wit.gameplan.adapters.GameAdapter
import ie.wit.gameplan.adapters.GameListener
import ie.wit.gameplan.databinding.FragmentGameListBinding

import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.UserModel
import timber.log.Timber


class GameListFragment : Fragment(), GameListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentGameListBinding? = null
    private val fragBinding get() = _fragBinding!!

    var user = UserModel()

    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentGameListBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        fragBinding.recyclerView.adapter = GameAdapter(app.games.findAll(), this)


        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GameListFragment().apply {
                arguments = Bundle().apply {}
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
    override fun onResume() {
        super.onResume()
    }

    fun showGames(games: List<GameModel>) {
        fragBinding.recyclerView.adapter = GameAdapter(games, this)
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun loadGames() {
        showGames(app.games.findAll())
    }

    override fun onGameClick(game: GameModel) {
        //val launcherIntent = Intent(this, GameViewActivity::class.java)
        //launcherIntent.putExtra("game_view", game)
        //launcherIntent.putExtra("user", user)
        //refreshIntentLauncher.launch(launcherIntent)
    }

}