package ie.wit.gameplan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseUser
import ie.wit.gameplan.R
import ie.wit.gameplan.databinding.HomeBinding
import ie.wit.gameplan.databinding.NavHeaderBinding
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.UserModel
import ie.wit.gameplan.ui.auth.LoggedInViewModel
import ie.wit.gameplan.ui.auth.Login
import timber.log.Timber

class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel : LoggedInViewModel
    private lateinit var navHeaderBinding : NavHeaderBinding

    //var user = UserModel()
    var game = GameModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout

        //user = intent.extras?.getParcelable("user")!!
        //Timber.i("Logged in: $user")


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.gameFragment, R.id.gameListFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

    }

    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        var headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        navHeaderBinding.navHeaderEmail.text = currentUser.email
    }
    //override fun onSupportNavigateUp(): Boolean {
    //    val navController = findNavController(R.id.nav_host_fragment)
    //    return NavigationUI.navigateUp(navController, drawerLayout)
    //}

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}