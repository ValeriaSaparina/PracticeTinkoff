package com.example.travels.ui

import com.example.travels.ui.places.PlacesFragment
import com.example.travels.ui.signIn.SignInFragment
import com.example.travels.ui.signUp.SignUpFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun SignIn() = FragmentScreen { SignInFragment() }
    fun SignUp() = FragmentScreen { SignUpFragment() }
    fun Places() = FragmentScreen { PlacesFragment() }

}