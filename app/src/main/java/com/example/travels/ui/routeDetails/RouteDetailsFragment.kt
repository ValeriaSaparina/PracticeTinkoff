package com.example.travels.ui.routeDetails

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels.R
import com.example.travels.databinding.FragmentRouteDetailsBinding
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.MainActivity
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.routeDetails.adapter.DetailsAdapter
import com.example.travels.ui.routeDetails.model.RouteDetailsUIModel
import com.example.travels.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RouteDetailsFragment : BaseFragment(R.layout.fragment_route_details) {
    private val viewBinding by viewBinding(FragmentRouteDetailsBinding::bind)
    private val viewModel: RouteDetailsViewModel by viewModels()
    private var routeId: String? = null
    private val detailsAdapter = DetailsAdapter(::sendReview)

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
                    updateItem(it)
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
            updateItem(details)
        }
    }

    private fun updateItem(item: DisplayableItem) {
        val newList =
            detailsAdapter.items.orEmpty().toMutableList().apply {
                if (size != 0) {
                    removeAt(0)
                }
                add(0, item)
            }
        detailsAdapter.items = newList
        detailsAdapter.notifyItemChanged(0)
    }

    private fun updateItem(items: List<DisplayableItem>) {
        val newList =
            detailsAdapter.items.orEmpty().toMutableList().apply { addAll(items) }
        detailsAdapter.items = newList
        detailsAdapter.notifyItemInserted(newList.size - 1)
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