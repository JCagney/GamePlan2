package ie.wit.gameplan.ui.list

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.gameplan.R
import ie.wit.gameplan.adapters.GameAdapter
import ie.wit.gameplan.adapters.GameListener
import ie.wit.gameplan.databinding.FragmentGameListBinding

import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.UserModel


class GameListFragment : Fragment(), GameListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentGameListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var navController: NavController

    private lateinit var gameListViewModel: GameListViewModel

    //var user = UserModel()

    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentGameListBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        activity?.title = getString(R.string.app_name)

        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        //fragBinding.recyclerView.adapter = GameAdapter(app.games.findAll(), this)

        gameListViewModel = ViewModelProvider(this).get(GameListViewModel::class.java)
        gameListViewModel.observableGameList.observe(viewLifecycleOwner, Observer { games ->
            games?.let { render(games) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = GameListFragmentDirections.actionGameListFragmentToGameFragment(null)
            findNavController().navigate(action)
        }

        fragBinding.filter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            //filter for games where the title OR description contains the entered text
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    var filteredGames = gameListViewModel.observableGameList.value!!.filter { g ->
                        g.title.lowercase().contains(
                            p0.toString().lowercase()
                        ) ||
                                g.description.lowercase().contains(
                                    p0.toString().lowercase()
                                )
                    }
                    render(filteredGames)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        )

        //navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)


        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GameListFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

            return NavigationUI.onNavDestinationSelected(
                item,
                requireView().findNavController()
            ) || super.onOptionsItemSelected(item)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onGameClick(game: GameModel) {
        val action = GameListFragmentDirections.actionGameListFragmentToGameViewFragment(game.uid)
        findNavController().navigate(action)
    }

    private fun render(gameList: List<GameModel>) {
        fragBinding.recyclerView.adapter = GameAdapter(gameList, this)
        if (gameList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.gamesNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.gamesNotFound.visibility = View.GONE
        }
    }




}