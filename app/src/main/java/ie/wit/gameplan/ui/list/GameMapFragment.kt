package ie.wit.gameplan.ui.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.FragmentGameMapBinding
import ie.wit.gameplan.databinding.FragmentMapBinding
import ie.wit.gameplan.ui.auth.LoggedInViewModel
import timber.log.Timber


class GameMapFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var mapView: MapView
    private var _fragBinding: FragmentGameMapBinding? = null
    private val fragBinding get() = _fragBinding!!
    //private lateinit var gameListViewModel: GameListViewModel

    private val gameListViewModel : GameListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        _fragBinding = FragmentGameMapBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        mapView = fragBinding.mapview
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync{
            map = it
            configureMap()
        }



        return root
    }

    fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                gameListViewModel.liveFirebaseUser.value = firebaseUser
                gameListViewModel.load()
            }
        })
        gameListViewModel.observableGameList.observe(viewLifecycleOwner, Observer { games ->
            games?.let {
                Timber.i(games.toString())
                map.clear()
                games.forEach {
                    val loc = LatLng(it.lat, it.lng)
                    val options = MarkerOptions().title(it.title).snippet(it.date).position(loc)
                    map.addMarker(options).tag = it.uid
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))

                }
            }


        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.toggleGames) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleGames: SwitchCompat = item.actionView.findViewById(R.id.toggleButton)
        toggleGames.isChecked = false

        toggleGames.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                gameListViewModel.loadAll()
                Toast.makeText(activity, "Showing All Games", Toast.LENGTH_LONG).show()

            }
            else
            {
                gameListViewModel.load()
                Toast.makeText(activity, "Showing Your Games", Toast.LENGTH_LONG).show()
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }


}