package com.example.travels.ui.places

import androidx.recyclerview.widget.DiffUtil
import com.example.travels.ui.places.model.PlaceUiModel

class PlacesDiffCallback : DiffUtil.ItemCallback<PlaceUiModel>() {
    override fun areItemsTheSame(oldItem: PlaceUiModel, newItem: PlaceUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlaceUiModel, newItem: PlaceUiModel): Boolean {
        return oldItem == newItem
    }

}