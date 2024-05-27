package com.example.travels.ui.routes

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels.R
import com.example.travels.databinding.FragmentRoutesBinding
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.routes.model.RouteUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutesFragment : BaseFragment(R.layout.fragment_routes) {

    private val viewBinding by viewBinding(FragmentRoutesBinding::bind)
    private val viewModel: RoutesViewModel by viewModels()
    private val routesAdapter = RoutesAdapter(
        onFavIcClicked = ::onFavIcClicked,
        onItemClicked = ::onItemClicked
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initAdapter()
        initObservers()
    }

    private fun initObservers() {
        with(viewModel) {
            result.observe {
                it?.let {
                    routesAdapter.submitList(it)
                    if (it.isEmpty()) {
                        showToast(resources.getString(R.string.not_found))
                    }
                }
            }

            error.observe {
                it?.let {
                    showToast(it.message.toString())
                }
            }

            process.observe {
                it?.let {
                    with(viewBinding) {
                        progressBar.isVisible = it
                        favRoutesRv.isVisible = !it
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        with(viewBinding.favRoutesRv) {
            adapter = routesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initListeners() {
        with(viewBinding) {
            addRoutesFb.setOnClickListener {
                router.navigateTo(Screens.AddRoute())
            }
            searchIc.setOnClickListener {
                viewModel.searchRoutes(queryEt.text.toString())
            }
        }
    }

    private fun onItemClicked(item: RouteUIModel) {
        router.navigateTo(Screens.RouteDetails(item.id))
    }

    private fun onFavIcClicked(item: RouteUIModel) {
        viewModel.onFavIcClicked(item)
    }


    companion object {

        const val TAG = "ROUTES_FRAGMENT"
        fun newInstance(): RoutesFragment {
            return RoutesFragment()
        }
    }
}