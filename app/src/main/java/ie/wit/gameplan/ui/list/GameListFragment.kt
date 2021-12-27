package ie.wit.gameplan.ui.list

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
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
import ie.wit.gameplan.ui.auth.LoggedInViewModel
import ie.wit.gameplan.utils.createLoader
import ie.wit.gameplan.utils.hideLoader
import ie.wit.gameplan.utils.showLoader


class GameListFragment : Fragment(), GameListener {


    private var _fragBinding: FragmentGameListBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var gameListViewModel: GameListViewModel
    lateinit var loader : AlertDialog

    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //app = activity?.application as MainApp
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

        loader = createLoader(requireActivity())

        gameListViewModel = ViewModelProvider(this).get(GameListViewModel::class.java)
        showLoader(loader,"Downloading Games")
        gameListViewModel.observableGameList.observe(viewLifecycleOwner, Observer { games ->
            games?.let {
                render(games)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = GameListFragmentDirections.actionGameListFragmentToGameFragment(null)
            findNavController().navigate(action)
        }

        setSwipeRefresh()

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

        val item = menu.findItem(R.id.toggleGames) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleGames: SwitchCompat = item.actionView.findViewById(R.id.toggleButton)
        toggleGames.isChecked = false

        toggleGames.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) gameListViewModel.loadAll()
            else gameListViewModel.load()
        }

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

        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                gameListViewModel.liveFirebaseUser.value = firebaseUser
                gameListViewModel.load()
            }
        })
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

    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            //showLoader(loader,"Downloading Games")
            gameListViewModel.load()
            checkSwipeRefresh()

        }
    }

    fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }




}