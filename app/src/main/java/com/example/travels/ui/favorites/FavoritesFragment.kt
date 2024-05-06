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
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.places.model.PlaceUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment(R.layout.fragment_favorites) {

    private var viewBinding: FragmentFavoritesBinding? = null
    private val viewModel: FavoritePlacesViewModel by viewModels()
    private val favPlacesAdapter = FavoritePlacesAdapter(
        onFavIcClicked = ::onFavIcClicked,
        onItemClicked = ::onItemClicked
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
        initAdapters()
        initListeners()
        initObservers()

        viewModel.getFavoritePlaces()
    }

    private fun initObservers() {
        with(viewModel) {
            error.observe {
                if (it != null) {
                    showToast(it.name)
                }
            }
            result.observe {
                favPlacesAdapter.submitList(it)
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

                }
            }
        }
    }

    private fun onItemClicked(item: PlaceUiModel) {
//        router.navigateTo(Screens.PlaceDetails())
    }

    private fun onFavIcClicked(item: PlaceUiModel) {
        viewModel.onFavIcClicked(item)
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