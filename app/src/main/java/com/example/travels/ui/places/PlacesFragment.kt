package com.example.travels.ui.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travels.databinding.FragmentPlacesBinding
import com.example.travels.ui.MainActivity
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.places.model.PlaceUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesFragment : BaseFragment() {

    private var viewBinding: FragmentPlacesBinding? = null
    private val viewModel: PlacesViewModel by viewModels()

    private val placesAdapter = PlacesAdapter(::onItemClicked, ::onFavIcClicked)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPlacesBinding.inflate(inflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).isBottomNavVisible = true
        initAdapter()
        initListeners()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            error.observe {
                if (it != null) {
                    showToast(it.name)
                }
            }
            result.observe {
                placesAdapter.submitData(it)
            }
        }
    }

    private fun initListeners() {
        viewBinding?.run {
            searchIc.setOnClickListener {
                viewModel.searchPlaces(queryEt.text.toString())
            }
        }
    }

    private fun onItemClicked(item: PlaceUiModel) {
//        router.navigateTo(Screens.PlaceDetails())
    }

    private fun onFavIcClicked(item: PlaceUiModel) {
        viewModel.onFavIcClicked(item)
    }


    private fun initAdapter() {
        viewBinding?.run {
            with(placesRv) {
                layoutManager = GridLayoutManager(context, 2)
                adapter = placesAdapter.withLoadStateHeaderAndFooter(
                    header = PlacesLoaderStateAdapter(),
                    footer = PlacesLoaderStateAdapter(),
                )
            }
        }
    }

    companion object {
        const val TAG = "PLACES_FRAGMENT"

        fun newInstance(): PlacesFragment {
            return PlacesFragment()
        }
    }
}
