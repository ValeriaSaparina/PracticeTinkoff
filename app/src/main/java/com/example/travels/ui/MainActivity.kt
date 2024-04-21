package com.example.travels.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travels.R
import com.example.travels.ui.App.Companion.router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navigator = object : AppNavigator(this, R.id.container) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.newRootScreen(Screens.Places()) // TODO: init screen
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.places -> {
                    router.replaceScreen(Screens.Places())
                    true
                }

                R.id.routes -> {
                    router.replaceScreen(Screens.Routes())
                    true
                }

                R.id.profile -> {
                    router.replaceScreen(Screens.Profile())
                    true
                }

                else -> false
            }
        }

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