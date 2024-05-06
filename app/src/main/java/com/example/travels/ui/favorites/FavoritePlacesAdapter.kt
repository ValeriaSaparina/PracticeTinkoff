package com.example.travels.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.travels.databinding.ItemPlaceBinding
import com.example.travels.ui.places.PlacesDiffCallback
import com.example.travels.ui.places.PlacesViewHolder
import com.example.travels.ui.places.model.PlaceUiModel
import javax.inject.Inject

class FavoritePlacesAdapter @Inject constructor(
    private val onItemClicked: (PlaceUiModel) -> Unit,
    private val onFavIcClicked: (PlaceUiModel) -> Unit,
) : ListAdapter<PlaceUiModel, FavoritePlacesAdapter.FavoritePlacesViewHolder>(PlacesDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlacesViewHolder =
        FavoritePlacesViewHolder(
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FavoritePlacesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class FavoritePlacesViewHolder(
        private val viewBinding: ItemPlaceBinding
    ) : PlacesViewHolder(
        onItemClicked,
        onFavIcClicked,
        viewBinding,
    ) {
        override fun bind(item: PlaceUiModel) {
            super.bind(item)
            with(viewBinding) {
                favIc.setOnClickListener {
                    onFavIcClicked(item)
                    val newList = currentList.toMutableList()
                    newList.removeAt(newList.indexOf(item))
                    submitList(newList)
                }
            }
        }

    }
}