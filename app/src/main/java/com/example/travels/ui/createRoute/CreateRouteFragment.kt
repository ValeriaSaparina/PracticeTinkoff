package com.example.travels.ui.createRoute

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels.R
import com.example.travels.databinding.FragmentCreateRouteBinding
import com.example.travels.ui.MainActivity
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.ui.routes.RoutesFragment
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
        viewBinding.toolbar.toolbar.title = resources.getString(R.string.create_route)
        viewModel.getFavPlaces() //TODO: fav icon
    }

    private fun initObservers() {
        with(viewModel) {
            error.observe {
                showInputError(it)
            }
            success.observe {
                if (it == true) {
//                    router.backTo(Screens.Routes())
//                    with(requireActivity() as MainActivity) {
//                        setItemSelected(1, true)
//                        select(RoutesFragment.TAG)
//                    }
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
            toolbar.toolbar.setNavigationOnClickListener {
//                router.backTo(Screens.Routes())
                with(requireActivity() as MainActivity) {
//                    setItemSelected(1, true)
                    select(RoutesFragment.TAG)
                }
            }

//            nameEt.validate(getString(R.string.fill_text)) { text -> text.isNotEmpty() }
//            descEt.validate(getString(R.string.fill_text)) { text -> text.isNotEmpty() }
        }
        this.id
    }

    private fun onFavIcClicked(item: PlaceUiModel) {

    }

    private fun onItemClicked(item: PlaceUiModel): Boolean {
        val result = viewModel.onPlaceClicked(item)
        Log.d("CREATE_ROUTE", "$result")
        return result
    }

    companion object {
        const val TAG: String = "CREATE ROUTE FRAGMENT"
    }

}