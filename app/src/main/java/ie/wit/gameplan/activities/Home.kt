package ie.wit.gameplan.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.ui.*
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.HomeBinding
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.UserModel
import timber.log.Timber

class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    var user = UserModel()
    var game = GameModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout

        user = intent.extras?.getParcelable("user")!!
        Timber.i("Logged in: $user")


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.gameFragment, R.id.gameListFragment, R.id.gameViewFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}