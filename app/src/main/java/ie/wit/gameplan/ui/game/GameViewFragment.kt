package ie.wit.gameplan.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import android.view.*
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.FragmentGameViewBinding
import ie.wit.gameplan.ui.auth.LoggedInViewModel
import timber.log.Timber


class GameViewFragment : Fragment(), OnMapReadyCallback {

    lateinit var app: MainApp
    private var _fragBinding: FragmentGameViewBinding? = null
    private val fragBinding get() = _fragBinding!!
    var game = GameModel()
    private val args by navArgs<GameViewFragmentArgs>()

    private lateinit var gameViewModel: GameViewModel

    private val loggedInViewModel : LoggedInViewModel by activityViewModels()



    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //app = activity?.application as MainApp
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGameViewBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.app_name)

        //game = args.game!!

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.observableGame.observe(viewLifecycleOwner, Observer { render() })
        gameViewModel.getGame(args.gameId!!)

        //fragBinding.gameTitle.setText(game.title)
        //fragBinding.description.setText(game.description)
        //fragBinding.date.setText("Game Date: ${game.date}")
        //fragBinding.creater.setText("Created by ${game.creator}")

        fragBinding.editGame.setOnClickListener {
            val action = GameViewFragmentDirections.actionGameViewFragmentToGameFragment(gameViewModel.observableGame.value as GameModel)
            findNavController().navigate(action)
        }

        fragBinding.deleteGame.setOnClickListener {
            if(loggedInViewModel.liveFirebaseUser.value!!.email == gameViewModel.observableGame.value!!.creator!!) {
                gameViewModel.delete(loggedInViewModel.liveFirebaseUser.value!!.uid, gameViewModel.observableGame.value!!.uid!!)
                val action = GameViewFragmentDirections.actionGameViewFragmentToGameListFragment()
                findNavController().navigate(action)
            }
        }

        mapView = fragBinding.mapview
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        //mapView.getMapAsync(this)

        return root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView.getMapAsync(this)
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GameViewFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_view_game, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(gameViewModel.observableGame.value!!.lat, gameViewModel.observableGame.value!!.lng)
        map.addMarker(MarkerOptions().position(loc).title(gameViewModel.observableGame.value!!.title))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, gameViewModel.observableGame.value!!.zoom))
    }

    override fun onResume() {
        super.onResume()
        gameViewModel.getGame(args.gameId!!)

    }

    private fun render() {

        fragBinding.gamevm = gameViewModel

        //only show edit & delete options if game belongs to logged in user
        if(loggedInViewModel.liveFirebaseUser.value!!.email == gameViewModel.observableGame.value!!.creator!!)
        {
            fragBinding.deleteGame.visibility = View.VISIBLE
            fragBinding.editGame.visibility = View.VISIBLE
        }
        else
        {
            fragBinding.deleteGame.visibility = View.GONE
            fragBinding.editGame.visibility = View.GONE
        }

    }




}