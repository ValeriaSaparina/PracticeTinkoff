package com.example.travels.ui.createRoute

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels.R
import com.example.travels.databinding.FragmentCreateRouteBinding
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.utils.validate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateRouteFragment : BaseFragment(R.layout.fragment_create_route) {

    private val viewBinding by viewBinding(FragmentCreateRouteBinding::bind)
    private val viewModel: CreateRouteViewModel by viewModels()
    private val placesAdapter = RoutePlacesAdapter(::onFavIcClicked, ::onItemClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRv()
        initObservers()
        initData()
    }

    private fun initData() {
        viewBinding.editToolbar.toolbar.title = resources.getString(R.string.create_route)
        viewModel.getFavPlaces() //TODO: fav icon
    }

    private fun initObservers() {
        with(viewModel) {
            process.observe {
                it?.let {
                    with(viewBinding) {
                        contentGroup.isVisible = !it
                        progressBar.isVisible = it
                    }
                }
            }
            error.observe {
                showInputError(it)
            }
            success.observe {
                if (it == true) {
                    router.backTo(Screens.Routes())
                }
            }
            favPlaces.observe {
                if (it != null) {
                    placesAdapter.submitList(it)
                }
            }
        }
    }

    private fun initRv() {
        with(viewBinding.placesRv) {
            adapter = placesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initListeners() {
        with(viewBinding) {
            createBtn.setOnClickListener {
                viewModel.createRoute(
                    nameEt.text.toString(),
                    descEt.text.toString(),
                )
            }
            editToolbar.toolbar.setNavigationOnClickListener {
                router.backTo(Screens.Routes())
            }

            nameEt.validate(getString(R.string.empty_value)) { text -> text.isNotEmpty() }
            descEt.validate(getString(R.string.empty_value)) { text -> text.isNotEmpty() }
        }
    }

    private fun onFavIcClicked(item: PlaceUiModel) {

    }

    private fun onItemClicked(item: PlaceUiModel): Boolean {
        val result = viewModel.onPlaceClicked(item)
        return result
    }

    companion object {
        const val TAG: String = "CREATE ROUTE FRAGMENT"
    }

}