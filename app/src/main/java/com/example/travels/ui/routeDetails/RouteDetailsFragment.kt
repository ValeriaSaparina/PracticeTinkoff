package com.example.travels.ui.routeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travels.R
import com.example.travels.databinding.FragmentRouteDetailsBinding
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.MainActivity
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.routeDetails.adapter.DetailsAdapter
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteDetailsFragment : BaseFragment(R.layout.fragment_route_details) {
    private var viewBinding: FragmentRouteDetailsBinding? = null
    private val viewModel: RouteDetailsViewModel by viewModels()
    private var routeId: String? = null
    private val detailsAdapter = DetailsAdapter(::sendReview)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRouteDetailsBinding.inflate(inflater)
        return viewBinding?.root
    }

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
        viewBinding?.detailsRv?.run {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initObservers() {
        with(viewModel) {
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

    private fun showData(route: RouteUIModel) {
        viewBinding?.run {
            toolbar.toolbar.title = route.name
            updateItem(route)
        }
    }

    private fun updateItem(item: DisplayableItem) {
        val newList =
            detailsAdapter.items.orEmpty().toMutableList().apply { add(item) }
        detailsAdapter.items = newList
        detailsAdapter.notifyItemInserted(newList.size - 1)
    }

    private fun updateItem(items: List<DisplayableItem>) {
        val newList =
            detailsAdapter.items.orEmpty().toMutableList().apply { addAll(items) }
        detailsAdapter.items = newList
        detailsAdapter.notifyItemInserted(newList.size - 1)
    }

    private fun initListeners() {
        viewBinding?.let {
            with(it) {
                toolbar.toolbar.setNavigationOnClickListener {
                    router.backTo(Screens.Places())
                }
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