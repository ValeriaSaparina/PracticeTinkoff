package com.example.travels.ui.routes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.travels.databinding.ItemRouteBinding
import com.example.travels.ui.routes.model.RouteUIModel

class RoutesAdapter(
    private val onFavIcClicked: (RouteUIModel) -> Unit,
    private val onItemClicked: (RouteUIModel) -> Unit,
) : ListAdapter<RouteUIModel, RoutesViewHolder>(RoutesDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutesViewHolder =
        RoutesViewHolder(
            viewBinding = ItemRouteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onFavIcClicked = onFavIcClicked,
            onItemClicked = onItemClicked
        )

    override fun onBindViewHolder(holder: RoutesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}