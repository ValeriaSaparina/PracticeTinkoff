package com.example.travels.ui.routes

import androidx.recyclerview.widget.DiffUtil
import com.example.travels.ui.routes.model.RouteUIModel

class RoutesDiffCallback : DiffUtil.ItemCallback<RouteUIModel>() {
    override fun areItemsTheSame(oldItem: RouteUIModel, newItem: RouteUIModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: RouteUIModel, newItem: RouteUIModel): Boolean =
        oldItem == newItem

}