package ie.wit.gameplan.ui.game

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.DatePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI

import com.google.android.material.snackbar.Snackbar
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.FragmentGameBinding
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.Location
import ie.wit.gameplan.ui.auth.LoggedInViewModel

import timber.log.Timber
import timber.log.Timber.i
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class GameFragment : Fragment() {

    lateinit var app: MainApp
    private var _fragBinding: FragmentGameBinding? = null
    private val fragBinding get() = _fragBinding!!
    //lateinit var navController: NavController
    private val args by navArgs<GameFragmentArgs>()

    private lateinit var gameViewModel: GameViewModel

    private lateinit var loggedInViewModel : LoggedInViewModel

    var game = GameModel()

    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //app = activity?.application as MainApp
        //setHasOptionsMenu(true)
        //navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGameBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.app_name)

        var user = ""

        if (args.game != null) {
            game = args.game!!
            edit = true
            fragBinding.gameTitle.setText(game.title)
            fragBinding.description.setText(game.description)
            fragBinding.btnAdd.setText(R.string.save_game)

        }

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        //gameViewModel.observableGame.observe(viewLifecycleOwner, Observer { render() })

        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null)
                user = loggedInViewModel.liveFirebaseUser.value!!.email!!
        })


        fragBinding.btnAdd.setOnClickListener() {
            game.title = fragBinding.gameTitle.text.toString()
            game.description = fragBinding.description.text.toString()
            if (game.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_game_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    gameViewModel.updateGame(loggedInViewModel.liveFirebaseUser.value!!.uid!!, game.uid!!, game.copy())
                    findNavController().navigate(R.id.gameListFragment)

                } else {
                    //for a new game, associate the current user as the creator of the game
                    game.creator = user
                    //game.creatorPic = user.image
                    gameViewModel.addGame(loggedInViewModel.liveFirebaseUser, game.copy())
                    findNavController().navigate(R.id.gameListFragment)
                }
            }
        }

        fragBinding.gameLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (game.zoom != 0f) {
                location.lat = game.lat
                location.lng = game.lng
                location.zoom = game.zoom
            }
            val action = GameFragmentDirections.actionGameFragmentToMapFragment(location)
            findNavController().navigate(action)

        }

        // set the date of the game
        val datePicker = fragBinding.datePicker
        val date = LocalDate.parse(game.date, DateTimeFormatter.ofLocalizedDate(
            FormatStyle.LONG))
        datePicker.init(date.year, date.monthValue -1, date.dayOfMonth)
        {
                datePicker, year, month, day ->
                game.date = LocalDate.of(year, month + 1, day).format(
                DateTimeFormatter.ofLocalizedDate(
                FormatStyle.LONG))

        }
        return root;
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //add an entry to SavedStateHandle to store location data while in Map Fragment
        //https://developer.android.com/guide/navigation/navigation-programmatic#returning_a_result
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Location>("key")?.observe(
            viewLifecycleOwner) { result ->

            game.lat = result.lat
            game.lng = result.lng
            game.zoom = result.zoom
        }
    }

    //override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    //    inflater.inflate(R.menu.menu_game, menu)
    //    super.onCreateOptionsMenu(menu, inflater)
    //}

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