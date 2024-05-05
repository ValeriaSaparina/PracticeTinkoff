package com.example.travels.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.travels.R
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.places.PlacesFragment
import com.example.travels.ui.profile.ProfileFragment
import com.example.travels.ui.routes.RoutesFragment
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navigator = object : AppNavigator(this, R.id.container) {}

    private val viewModel: AuthViewModel by viewModels()

    private val bottomNav: BottomNavigationView by lazy { findViewById(R.id.bottom_nav) }
    var isBottomNavVisible: Boolean
        get() = bottomNav.isVisible
        set(value) {
            bottomNav.isVisible = value
        }

    var bottomNavVisibility
        get() = bottomNav.visibility
        set(value) {
            bottomNav.visibility = value
        }

    var bottomNavItemSelected
        get() = bottomNav.menu.getItem(0).isChecked
        set(value) {
            bottomNav.menu.getItem(0).isChecked = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            lifecycleScope.launch {
                repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                    with(viewModel) {
                        getAuthStatus()
                        authorizationStatus.collect {
                            it?.let {
                                if (it) {
                                    select(PlacesFragment.TAG)
                                } else {
                                    router.newRootScreen(Screens.SignIn())
                                }
                            }
                        }
                    }
                }
            }
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.places -> {
                    select(PlacesFragment.TAG)
                    true
                }

                R.id.routes -> {
                    select(RoutesFragment.TAG)
                    true
                }

                R.id.profile -> {
                    select(ProfileFragment.TAG)
                    true
                }

                else -> false
            }
        }

    }

    internal fun select(tag: String) {
        val fm = supportFragmentManager
        var currentFragment: Fragment? = null
        val fragments = fm.fragments
        for (f in fragments) {
            if (f.isVisible) {
                currentFragment = f
                break
            }
        }

        val newFragment = fm.findFragmentByTag(tag)
        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return
        val transaction = fm.beginTransaction()
        if (newFragment == null) {
            when (tag) {
                PlacesFragment.TAG -> transaction.add(
                    R.id.container,
                    PlacesFragment.newInstance(),
                    tag
                )

                ProfileFragment.TAG -> transaction.add(
                    R.id.container,
                    ProfileFragment.newInstance(),
                    tag
                )

                RoutesFragment.TAG -> transaction.add(
                    R.id.container,
                    RoutesFragment.newInstance(),
                    tag
                )
            }
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commitNow()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        App.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.navigatorHolder.removeNavigator()
    }
}
