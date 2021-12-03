package ie.wit.gameplan.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
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

    var user = UserModel()

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
        fragBinding.recyclerView.adapter = GameAdapter(app.games.findAll(), this)

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
        if (item.itemId == R.id.item_logout) {
            activity?.finish()
            return super.onOptionsItemSelected(item)
        }
        else {
            return NavigationUI.onNavDestinationSelected(
                item,
                requireView().findNavController()
            ) || super.onOptionsItemSelected(item)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onGameClick(game: GameModel) {
        val action = GameListFragmentDirections.actionGameListFragmentToGameViewFragment(game)
        findNavController().navigate(action)
    }


}