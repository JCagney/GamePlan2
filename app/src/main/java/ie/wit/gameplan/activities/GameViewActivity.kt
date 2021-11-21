package ie.wit.gameplan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.ActivityGameViewBinding
import ie.wit.gameplan.main.MainApp
import ie.wit.gameplan.models.GameModel
import timber.log.Timber

class GameViewActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityGameViewBinding
    var game = GameModel()
    lateinit var app: MainApp
    private lateinit var editIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var map: GoogleMap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        game = intent.extras?.getParcelable("game_view")!!
        Timber.i("Viewing game $game")

        binding.gameTitle.setText(game.title)
        binding.description.setText(game.description)
        binding.date.setText("Game Date: ${game.date}")
        binding.creater.setText("Created by ${game.creator}")
        Picasso.get().load(game.creatorPic).resize(200,200).into(binding.imageCreator)

        binding.editGame.setOnClickListener {
            val launcherIntent = Intent(this, GameActivity::class.java)
                .putExtra("game_edit", game)
            editIntentLauncher.launch(launcherIntent)
        }

        binding.deleteGame.setOnClickListener {
            Snackbar.make(it, "Delete Game?", Snackbar.LENGTH_LONG)
                .setAction(
                    "Delete"
                ) {
                    app.games.delete(game.id)
                    finish()
                }.show()
        }

        registerEditCallback()


        //create the map fragment object and then call the getMapAsync method to trigger the onMapReady function call
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_view_game, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //when (item.itemId) {
         //   R.id.item_list -> {
         //       finish()
         //   }
        //}
        return super.onOptionsItemSelected(item)
    }

    private fun registerEditCallback() {
        editIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Received edit")
                            game =
                                result.data!!.extras?.getParcelable<GameModel>("game")!!
                            binding.gameTitle.setText(game.title)
                            binding.description.setText(game.description)
                            binding.date.setText(game.date)
                            //Picasso.get().load(game.creatorPic).resize(200,200).into(binding.imageCreator)
                            map.clear()
                            val loc = LatLng(game.lat, game.lng)
                            map.addMarker(MarkerOptions().position(loc).title(game.title))
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, game.zoom))
                        }
                    }

                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(game.lat, game.lng)
        map.addMarker(MarkerOptions().position(loc).title(game.title))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, game.zoom))
    }
}


