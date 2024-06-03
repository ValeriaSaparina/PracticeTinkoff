package com.example.travels.ui.favoritesRoutes

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels.R
import com.example.travels.databinding.FragmentFavoriteItemsBinding
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.favorites.FavoritesRoutesAdapter
import com.example.travels.ui.routes.model.RouteUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRoutesFragment : BaseFragment(R.layout.fragment_favorite_items) {

    private val viewBinding by viewBinding(FragmentFavoriteItemsBinding::bind)
    private val viewModel: FavoriteRoutesViewModel by viewModels()
    private val routesAdapter = FavoritesRoutesAdapter(::onItemClicked, ::onFavIcClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding.favoritesItemsToolbar.toolbar) {
            title = resources.getString(R.string.favorites)
            setNavigationOnClickListener {
                router.backTo(Screens.Favorites())
            }
        }
        initAdapter()
        initObservers()
        viewModel.getFavPlaces()
        initListeners()
    }

    private fun initAdapter() {
        with(viewBinding.favoritePlacesRv) {
            adapter = routesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initListeners() {

    }

    private fun onItemClicked(route: RouteUIModel) {
        router.navigateTo(Screens.RouteDetails(route.id))
    }

    private fun onFavIcClicked(route: RouteUIModel) {
        viewModel.onFavIcClicked(route)
        routesAdapter.submitList(routesAdapter.currentList.toMutableList().apply { remove(route) })
    }

    private fun initObservers() {
        with(viewModel) {
            error.observe {
                it?.let {
                    showToast(it.name)
                }
            }

            process.observe {
                it?.let {
                    viewBinding.progressBar.isVisible = it
                    viewBinding.favoritePlacesRv.isVisible = !it
                }
            }

            placesResult.observe {
                routesAdapter.submitList(it)
            }
        }
    }
}