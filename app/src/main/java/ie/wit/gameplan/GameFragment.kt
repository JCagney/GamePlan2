package ie.wit.gameplan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import ie.wit.gameplan.activities.MapActivity
import ie.wit.gameplan.databinding.FragmentGameBinding
import ie.wit.gameplan.databinding.FragmentGameViewBinding
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.Location
import ie.wit.gameplan.models.UserModel


class GameFragment : Fragment() {

    lateinit var app: MainApp
    private var _fragBinding: FragmentGameBinding? = null
    private val fragBinding get() = _fragBinding!!
    //lateinit var navController: NavController

    var game = GameModel()
    var user = UserModel()

    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
        //navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGameBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.app_name)

        fragBinding.btnAdd.setOnClickListener() {
            game.title = fragBinding.gameTitle.text.toString()
            game.description = fragBinding.description.text.toString()
            if (game.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_game_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.games.update(game.copy())
                    findNavController().navigate(R.id.gameListFragment)


                } else {
                    //for a new game, associate the current user as the creator of the game
                    user = activity?.intent?.extras?.getParcelable("user")!!
                    game.creator = "${user.firstName} ${user.lastName}"
                    game.creatorPic = user.image
                    app.games.create(game.copy())
                    findNavController().navigate(R.id.gameListFragment)
                }
            }
        }

        fragBinding.gameLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            val action = GameFragmentDirections.actionGameFragmentToMapFragment(location)
            findNavController().navigate(action)



        }


        return root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GameFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}