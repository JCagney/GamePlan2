package ie.wit.gameplan.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import android.view.*
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
import ie.wit.gameplan.activities.GameActivity
import ie.wit.gameplan.databinding.FragmentGameViewBinding
import ie.wit.gameplan.models.Location
import ie.wit.gameplan.ui.list.GameListFragmentDirections


class GameViewFragment : Fragment(), OnMapReadyCallback {

    lateinit var app: MainApp
    private var _fragBinding: FragmentGameViewBinding? = null
    private val fragBinding get() = _fragBinding!!
    var game = GameModel()
    private val args by navArgs<GameViewFragmentArgs>()



    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGameViewBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.app_name)

        game = args.game!!

        fragBinding.gameTitle.setText(game.title)
        fragBinding.description.setText(game.description)
        fragBinding.date.setText("Game Date: ${game.date}")
        fragBinding.creater.setText("Created by ${game.creator}")

        fragBinding.editGame.setOnClickListener {
            val action = GameViewFragmentDirections.actionGameViewFragmentToGameFragment(game)
            findNavController().navigate(action)
        }

        mapView = fragBinding.mapview
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this)

        return root;
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

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(game.lat, game.lng)
        map.addMarker(MarkerOptions().position(loc).title(game.title))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, game.zoom))
    }



}