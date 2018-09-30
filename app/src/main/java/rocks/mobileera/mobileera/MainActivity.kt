package rocks.mobileera.mobileera

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import rocks.mobileera.mobileera.adapters.interfaces.AddToFavoritesCallback
import rocks.mobileera.mobileera.adapters.interfaces.SessionCallback
import rocks.mobileera.mobileera.adapters.interfaces.SpeakerCallback
import kotlinx.android.synthetic.main.activity_main.*
import rocks.mobileera.mobileera.fragments.SessionFragment
import rocks.mobileera.mobileera.model.Session
import rocks.mobileera.mobileera.model.Speaker

class MainActivity : AppCompatActivity(), SpeakerCallback, SessionCallback, AddToFavoritesCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolBar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.let { bottomNavView ->
            NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)
        }

        setupActionBarWithNavController(this, findNavController(hostFragment))
    }

    override fun onSupportNavigateUp() =
            findNavController(hostFragment).navigateUp()

    override fun onSpeakerClick(speaker: Speaker?, action: Int) {
        speaker?.let { value ->
            val bundle = SpeakerActivity.createBundle(value)
            NavHostFragment.findNavController(hostFragment).navigate(action, bundle)
        }
    }

    override fun onSessionClick(session: Session?) {
        session?.let { value ->
            val bundle = SessionFragment.createBundle(value)
            NavHostFragment.findNavController(hostFragment).navigate(R.id.action_navigation_schedule_to_sessionFragment, bundle)
        }
    }

    override fun onAddToFavoritesClick(session: Session?) {
        applicationContext?.let {context ->
            session?.toggleFavorites(context)
        }
    }
}
