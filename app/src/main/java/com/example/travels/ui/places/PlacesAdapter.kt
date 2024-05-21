package com.example.travels.ui.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.travels.databinding.ItemPlaceBinding
import com.example.travels.ui.places.model.PlaceUiModel

class PlacesAdapter(
    private val onItemClicked: (PlaceUiModel) -> Unit,
    private val onFavIcClicked: (PlaceUiModel) -> Unit
) :
    PagingDataAdapter<PlaceUiModel, PlacesViewHolder>(PlacesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder =
        PlacesViewHolder(
            onItemClicked,
            onFavIcClicked,
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}