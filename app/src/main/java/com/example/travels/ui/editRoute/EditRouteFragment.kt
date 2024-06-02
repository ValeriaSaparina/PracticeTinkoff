package com.example.travels.ui.editRoute

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
import com.example.travels.ui.createRoute.RoutePlacesAdapter
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.Constants
import com.example.travels.utils.validate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRouteFragment : BaseFragment(R.layout.fragment_create_route) {

    private val viewBinding by viewBinding(FragmentCreateRouteBinding::bind)
    private val viewModel: EditRouteViewModel by viewModels()
    private val placesAdapter = RoutePlacesAdapter(::onFavIcClicked, ::onItemClicked, ::isSelected)
    private var routeId: String? = null
    private var routeModel: RouteUIModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRv()
        initObservers()
        initData()
    }

    private fun initData() {
        with(viewBinding) {
            editToolbar.toolbar.title = resources.getString(R.string.edit_route)
            createBtn.text = resources.getString(R.string.edit_route)
        }
        arguments?.getString(Constants.ID)?.let {
            routeId = it
            viewModel.getRoute(it)
        }
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
                    router.backTo(Screens.RouteDetails(routeId!!))
                }
            }
            favPlaces.observe {
                if (it != null) {
                    placesAdapter.submitList(it)
                }
            }
            route.observe {
                if (it != null) {
                    routeModel = it
                    showData(it)
                }
            }
        }
    }

    private fun showData(route: RouteUIModel) {
        with(viewBinding) {
            with(route) {
                nameEt.setText(name)
                descEt.setText(type)
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
                viewModel.saveRoute(
                    routeModel!!.copy(name = nameEt.text.toString(), type = descEt.text.toString())
                )
            }
            editToolbar.toolbar.setNavigationOnClickListener {
                router.backTo(Screens.RouteDetails(routeId!!))
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

    private fun isSelected(id: String?): Boolean {
        return routeModel!!.placesId.contains(id)
    }

    companion object {

        const val TAG: String = "EDIT ROUTE FRAGMENT"
        fun newInstance(id: String) = EditRouteFragment().apply {
            arguments = Bundle().apply { putString(Constants.ID, id) }
        }
    }

}