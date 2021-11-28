package ie.wit.gameplan

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.squareup.picasso.Picasso
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import android.content.Intent
import android.view.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import ie.wit.gameplan.activities.Home
import ie.wit.gameplan.databinding.FragmentGameViewBinding


class GameViewFragment : Fragment() {

    lateinit var app: MainApp
    private var _fragBinding: FragmentGameViewBinding? = null
    private val fragBinding get() = _fragBinding!!
    var game = GameModel()
    private val args by navArgs<GameViewFragmentArgs>()

    lateinit var navController: NavController

    private lateinit var map: GoogleMap

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



}