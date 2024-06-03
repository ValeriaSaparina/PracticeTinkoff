package com.example.travels.ui.favoritesPlaces

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels.R
import com.example.travels.databinding.FragmentFavoriteItemsBinding
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.favorites.FavoritePlacesAdapter
import com.example.travels.ui.places.model.PlaceUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritePlacesFragment : BaseFragment(R.layout.fragment_favorite_items) {

    private val viewBinding by viewBinding(FragmentFavoriteItemsBinding::bind)
    private val viewModel: FavoritePlacesViewModel by viewModels()
    private val placesAdapter = FavoritePlacesAdapter(::onItemClicked, ::onFavIcClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.favoritesItemsToolbar.toolbar.title = resources.getString(R.string.favorites)
        initAdapter()
        initObservers()
        viewModel.getFavPlaces()
        initListeners()
    }

    private fun initAdapter() {
        with(viewBinding.favoritePlacesRv) {
            adapter = placesAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun initListeners() {

    }

    private fun onItemClicked(place: PlaceUiModel) {
//        router.navigateTo(Screens.PlacesDetails(place.id))
    }

    private fun onFavIcClicked(place: PlaceUiModel) {
        viewModel.onFavIcClicked(place)
        placesAdapter.submitList(placesAdapter.currentList.toMutableList().apply { remove(place) })
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
                placesAdapter.submitList(it)
            }
        }
    }
}