package com.example.travels.ui.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.travels.databinding.FragmentPlacesBinding
import com.example.travels.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesFragment : BaseFragment() {

    private var viewBinding: FragmentPlacesBinding? = null
    private val viewModel: PlacesViewModel by viewModels()


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
        initListeners()
        observe()
    }

    private fun observe() {
        viewModel.result.observe {
            viewBinding?.run {
                placesRv.adapter = PlacesAdapter(it?.result?.items ?: listOf())
            }
        }

        viewModel.error.observe {
            if (it != null) {
                showToast(it.name)
            }
        }
    }

    private fun initListeners() {
        viewBinding?.run {
            searchBtn.setOnClickListener {
                viewModel.onLoadPlacesClick(inputEt.text.toString())
            }
        }
    }

}