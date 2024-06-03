package com.example.travels.ui.routeDetails

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels.R
import com.example.travels.databinding.FragmentRouteDetailsBinding
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.MainActivity
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.routeDetails.adapter.DetailsAdapter
import com.example.travels.ui.routeDetails.model.RouteDetailsUIModel
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RouteDetailsFragment : BaseFragment(R.layout.fragment_route_details) {
    private val viewBinding by viewBinding(FragmentRouteDetailsBinding::bind)
    private val viewModel: RouteDetailsViewModel by viewModels()
    private var routeId: String? = null
    private val detailsAdapter = DetailsAdapter(::sendReview, ::onFavIcClicked, ::onAuthorClicked)
    private var routeDetails: RouteDetailsUIModel? = null

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).isBottomNavVisible = false
        initListeners()
        initObservers()

        initAdapter()

        routeId = arguments?.getString(Constants.ID)
        viewModel.getPlaceDetails(routeId!!)
    }

    private fun initAdapter() {
        viewBinding.detailsRv.run {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(context)
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

            routeResult.observe {
                if (it != null) {
                    showData(it)
                    routeDetails = it
                    viewModel.getAllReviews(routeId!!)
                }
            }
            error.observe {
                if (it != null) {
                    showToast(it.name)
                }
            }
            review.observe {
                if (it != null) {
                    updateItem(it, 1)
                    val reviewsSize = detailsAdapter.items.orEmpty().size
                    val newRating =
                        (routeDetails!!.route.rating * reviewsSize + it.rating.toFloat()) / (reviewsSize + 1)
                    routeDetails =
                        routeDetails!!.copy(route = routeDetails!!.route.copy(rating = newRating))
                    updateItem(routeDetails!!, 0)
                }
            }
            reviewResults.observe {
                if (!it.isNullOrEmpty()) {
                    updateItem(it)
                }
            }
        }
    }

    private fun sendReview(rating: String, text: String) {
        viewModel.sendReview(
            routeId!!,
            rating,
            text,
        )
    }

    private fun showData(details: RouteDetailsUIModel) {
        viewBinding.run {
            detailsToolbar.toolbar.title = details.route.name
            if (details.route.author.id == auth.uid) {
                editRouteFab.isVisible = true
            }
            updateItem(details, 0)
        }
    }

    private fun updateItem(item: DisplayableItem, index: Int) {
        val newList =
            detailsAdapter.items.orEmpty().toMutableList().apply {
                if (size > index) {
                    removeAt(index)
                }
                add(index, item)
            }
        detailsAdapter.items = newList
        detailsAdapter.notifyItemChanged(index)
    }

    private fun updateItem(items: List<DisplayableItem>) {
        if (detailsAdapter.items.orEmpty().size <= 1) {
            val newList =
                detailsAdapter.items.orEmpty().toMutableList().apply { addAll(items) }
            detailsAdapter.items = newList
            detailsAdapter.notifyItemInserted(newList.size - 1)
        }
    }

    private fun onFavIcClicked(item: RouteUIModel) {
        viewModel.onFavIcClicked(item)
    }

    private fun onAuthorClicked(user: UserModel) {
        router.navigateTo(Screens.UserDetails(user.id))
    }

    private fun initListeners() {
        with(viewBinding) {
            detailsToolbar.toolbar.setNavigationOnClickListener {
                router.backTo(Screens.Places())
            }
            editRouteFab.setOnClickListener {
                router.navigateTo(Screens.EditRoute(routeId!!))
            }
        }
    }


    companion object {
        private const val TAG = "PlaceDetailsFragment"
        fun newInstance(id: String) = RouteDetailsFragment().apply {
            arguments = Bundle().apply { putString(Constants.ID, id) }

        }
    }
}