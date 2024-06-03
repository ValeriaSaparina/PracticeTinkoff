package com.example.travels.ui

import com.example.travels.ui.createRoute.CreateRouteFragment
import com.example.travels.ui.editRoute.EditRouteFragment
import com.example.travels.ui.favorites.FavoritesFragment
import com.example.travels.ui.favoritesPlaces.FavoritePlacesFragment
import com.example.travels.ui.favoritesRoutes.FavoriteRoutesFragment
import com.example.travels.ui.myRoutes.MyRoutesFragment
import com.example.travels.ui.places.PlacesFragment
import com.example.travels.ui.profile.ProfileFragment
import com.example.travels.ui.routeDetails.RouteDetailsFragment
import com.example.travels.ui.routes.RoutesFragment
import com.example.travels.ui.signIn.SignInFragment
import com.example.travels.ui.signUp.SignUpFragment
import com.example.travels.ui.userDetails.UserDetailsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun SignIn() = FragmentScreen { SignInFragment() }
    fun SignUp() = FragmentScreen { SignUpFragment() }
    fun Places() = FragmentScreen { PlacesFragment() }
    fun Routes() = FragmentScreen { RoutesFragment() }
    fun Profile() = FragmentScreen { ProfileFragment() }

    //    fun PlaceDetails() = FragmentScreen { PlaceDetailsFragment() }
    fun AddRoute() = FragmentScreen { CreateRouteFragment() }
    fun EditRoute(id: String) = FragmentScreen { EditRouteFragment.newInstance(id) }
    fun RouteDetails(id: String) = FragmentScreen { RouteDetailsFragment.newInstance(id) }
    fun Favorites() = FragmentScreen { FavoritesFragment() }
    fun FavoritePlaces() = FragmentScreen { FavoritePlacesFragment() }
    fun FavoriteRoutes() = FragmentScreen { FavoriteRoutesFragment() }
    fun UserDetails(id: String) = FragmentScreen { UserDetailsFragment.newInstance(id) }
    fun MyRoutes() = FragmentScreen { MyRoutesFragment() }

}