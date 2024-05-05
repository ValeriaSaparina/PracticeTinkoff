package com.example.travels.ui

import com.example.travels.ui.places.PlacesFragment
import com.example.travels.ui.profile.ProfileFragment
import com.example.travels.ui.routes.RoutesFragment
import com.example.travels.ui.signIn.SignInFragment
import com.example.travels.ui.signUp.SignUpFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun SignIn() = FragmentScreen { SignInFragment() }
    fun SignUp() = FragmentScreen { SignUpFragment() }
    fun Places() = FragmentScreen { PlacesFragment() }
    fun Routes() = FragmentScreen { RoutesFragment() }
    fun Profile() = FragmentScreen { ProfileFragment() }
//    fun PlaceDetails() = FragmentScreen { PlaceDetailsFragment() }
//    fun AddRoute() = FragmentScreen { AddRouteFragment() }
//    fun RouteDetails(id: Long) = FragmentScreen { RouteDetailsFragmnet.newInstance(id) }

}