package com.example.travels.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travels.R
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navigator = object : AppNavigator(this, R.id.container) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            App.router.newRootScreen(Screens.Places()) // TODO: init screen
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