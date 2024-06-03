package com.example.travels.ui.userDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels.R
import com.example.travels.databinding.FragmentDetailsProfileBinding
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.routes.RoutesAdapter
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsFragment : BaseFragment(R.layout.fragment_details_profile) {

    private val viewBinding by viewBinding(FragmentDetailsProfileBinding::bind)
    private val viewModel by viewModels<UserDetailsViewModel>()
    private val routesAdapter = RoutesAdapter(::onFavIcClicked, ::onItemClicked)
    private var userId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.profileToolbar.toolbar.title = resources.getString(R.string.profile)
        initAdapter()
        initObservers()
        initData()
        initListeners()
    }

    private fun initData() {
        arguments?.getString(Constants.ID)?.let {
            userId = it
            viewModel.getUserDetails(it)
            viewModel.getUserRoutes(it)
        }
    }

    private fun initAdapter() {
        with(viewBinding.routesRv) {
            adapter = routesAdapter
            layoutManager = LinearLayoutManager(context)
        }
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
                    viewBinding.contentGroup.isVisible = !it
                }
            }

            routesResult.observe {
                it?.let {
                    routesAdapter.submitList(it)
                }
            }

            userResult.observe {
                it?.let {
                    showData(it)
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun showData(user: UserModel) {
        with(viewBinding) {
            nameTv.text = "${user.firstname} ${user.lastname}"
            emailTv.text = user.email
        }
    }

    private fun initListeners() {}

    private fun onItemClicked(route: RouteUIModel) {
        router.navigateTo(Screens.RouteDetails(route.id))
    }

    private fun onFavIcClicked(route: RouteUIModel) {
        viewModel.onFavIcClicked(route)
    }

    companion object {
        const val TAG: String = "CREATE ROUTE FRAGMENT"

        fun newInstance(id: String) = UserDetailsFragment().apply {
            arguments = Bundle().apply { putString(Constants.ID, id) }
        }
    }

}