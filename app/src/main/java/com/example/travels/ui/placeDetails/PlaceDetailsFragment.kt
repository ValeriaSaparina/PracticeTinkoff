package com.example.travels.ui.placeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travels.databinding.FragmentPlaceDetailsBinding
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.MainActivity
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDetailsFragment : BaseFragment() {
    private var viewBinding: FragmentPlaceDetailsBinding? = null
    private val viewModel: PlaceDetailsViewModel by viewModels()
    private var placeId: Long? = null
    private val detailsAdapter = DetailsAdapter(::sendReview)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPlaceDetailsBinding.inflate(inflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).isBottomNavVisible = false
        initListeners()
        initObservers()

        initAdapter()

        placeId = arguments?.getLong(Constants.ID)
        viewModel.getPlaceDetails(placeId!!)
        viewModel.getAllReviews(placeId!!)
    }

    private fun initAdapter() {
        viewBinding?.detailsRv?.run {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initObservers() {
        with(viewModel) {
            placeResult.observe {
                if (it != null) {
                    showData(it)
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
            placeId!!,
            rating,
            text,
        )
    }

    private fun showData(place: PlaceUiModel) {
        viewBinding?.run {
            toolbar.toolbar.title = place.name
            updateItem(place)
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
        fun newInstance(id: Long) = PlaceDetailsFragment().apply {
            arguments = Bundle().apply { putLong(Constants.ID, id) }

        }
    }
}