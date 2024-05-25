package com.example.travels.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travels.R
import com.example.travels.databinding.FragmentFavoritesBinding
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.MainActivity
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.ui.routes.model.RouteUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment(R.layout.fragment_favorites) {

    private var viewBinding: FragmentFavoritesBinding? = null
    private val viewModel: FavoritesViewModel by viewModels()
    private val favPlacesAdapter = FavoritePlacesAdapter(
        onFavIcClicked = ::onPlaceFavIcClicked,
        onItemClicked = ::onPlaceItemClicked
    )
    private val favRoutesAdapter = FavoritesRoutesAdapter(
        onFavIcClicked = ::onRouteFavIcClicked,
        onItemClicked = ::onRouteItemClicked
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentFavoritesBinding.inflate(inflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).isBottomNavVisible = false

        initAdapters()
        initListeners()
        initObservers()

        viewBinding?.toolbar?.toolbar?.title = resources.getString(R.string.favorites)

        viewModel.getFavoritePlaces()
        viewModel.getFavoriteRoutes()
    }

    private fun initObservers() {
        with(viewModel) {
            error.observe {
                if (it != null) {
                    showToast(it.name)
                }
            }
            resultPlaces.observe {
                favPlacesAdapter.submitList(it)
            }
            resultRoutes.observe {
                favRoutesAdapter.submitList(it)
            }
        }
    }

    private fun initAdapters() {
        viewBinding?.let {
            with(it) {
                with(favPlacesRv) {
                    adapter = favPlacesAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
                with(favRoutesRv) {
                    adapter = favRoutesAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
        }
    }

    private fun onPlaceItemClicked(item: PlaceUiModel) {
//        router.navigateTo(Screens.PlaceDetails())
    }

    private fun onPlaceFavIcClicked(item: PlaceUiModel) {
        viewModel.onPlaceFavIcClicked(item)
    }

    private fun onRouteItemClicked(item: RouteUIModel) {
        router.navigateTo(Screens.RouteDetails(item.id))
    }

    private fun onRouteFavIcClicked(item: RouteUIModel) {
        viewModel.onRouteFavIcClicked(item)
    }

    private fun initListeners() {
        viewBinding?.let {
            with(it) {
                allFavPlacesTv.setOnClickListener {
//                    router.navigateTo(Screens.FavoritePlaces)
                }
                allFavRoutesTv.setOnClickListener {
//                    router.navigateTo(Screens.FavoriteRoutes)
                }
                toolbar.toolbar.setNavigationOnClickListener {
                    router.backTo(Screens.Profile())
                }
            }
        }
    }

}