package com.example.travels.ui.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travels.R
import com.example.travels.databinding.FragmentPlacesBinding
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.places.model.ItemUiModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlacesFragment : BaseFragment() {

    private var viewBinding: FragmentPlacesBinding? = null
    private val viewModel: PlacesViewModel by viewModels()

    private var searchJob: Job? = null


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
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
            View.VISIBLE
        initAdapter()
        initListeners()
        observe()
    }

    private fun observe() {

        viewModel.error.observe {
            if (it != null) {
                showToast(it.name)
            }
        }

    }

    private fun initListeners() {
        viewBinding?.run {
            searchIc.setOnClickListener {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch(Dispatchers.IO) {
                    val result = viewModel.searchRepos(queryEt.text.toString())
                    result.collect {
                        (placesRv.adapter as PlacesAdapter).submitData(it)
                    }
                }
            }
        }
    }


    private fun onItemClicked(item: ItemUiModel) {
//        router.navigateTo(Screens.PlaceDetails())
    }

    private fun onFavIcClicked(item: ItemUiModel) {
        viewModel.onFavIcClicked(item)
    }


    private fun initAdapter() {
        viewBinding?.run {
            with(placesRv) {
                layoutManager = GridLayoutManager(context, 2)
                adapter = PlacesAdapter(::onItemClicked, ::onFavIcClicked)
            }
        }
    }

}