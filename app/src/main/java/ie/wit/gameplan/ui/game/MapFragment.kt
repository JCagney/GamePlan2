package ie.wit.gameplan.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.gameplan.models.Location
import com.google.android.gms.maps.MapView

import ie.wit.gameplan.databinding.FragmentMapBinding


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener,
GoogleMap.OnMarkerClickListener {

    //some help from here: https://newbedev.com/getmapasync-in-fragment

    private lateinit var map: GoogleMap
    var location = Location()
    private lateinit var mapView: MapView
    private var _fragBinding: FragmentMapBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val args by navArgs<MapFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        location = args.location

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentMapBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        fragBinding.btnSetLocation.setOnClickListener() {
            //val action = MapFragmentDirections.actionMapFragmentToGameFragment(location)
            //findNavController().navigate(action)
            findNavController().popBackStack()
        }
        mapView = fragBinding.mapview
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this)

        return root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Game")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        map.setOnMarkerDragListener(this)
        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom

    }



    override fun onMarkerClick(marker: Marker): Boolean {
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
        return false
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MapFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}