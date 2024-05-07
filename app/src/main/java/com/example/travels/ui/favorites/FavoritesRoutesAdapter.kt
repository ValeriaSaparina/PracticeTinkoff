package com.example.travels.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.travels.databinding.ItemRouteBinding
import com.example.travels.ui.routes.RoutesDiffCallback
import com.example.travels.ui.routes.RoutesViewHolder
import com.example.travels.ui.routes.model.RouteUIModel

class FavoritesRoutesAdapter(
    private val onItemClicked: (RouteUIModel) -> Unit,
    private val onFavIcClicked: (RouteUIModel) -> Unit,
) :
    ListAdapter<RouteUIModel, FavoritesRoutesAdapter.FavoriteRoutesViewHolder>(RoutesDiffCallback()) {
    inner class FavoriteRoutesViewHolder(
        private val onItemClicked: (RouteUIModel) -> Unit,
        private val onFavIcClicked: (RouteUIModel) -> Unit,
        private val viewBinding: ItemRouteBinding,
    ) :
        RoutesViewHolder(
            onItemClicked = onItemClicked,
            onFavIcClicked = onFavIcClicked,
            viewBinding = viewBinding,
        ) {
        override fun bind(item: RouteUIModel) {
            super.showData(item)
            initListeners(item)
        }

        private fun initListeners(item: RouteUIModel) {
            with(viewBinding) {
                root.setOnClickListener {
                    onItemClicked(item)
                }
                favIc.setOnClickListener {
                    onFavIcClicked(item)
                    val newList = currentList.toMutableList()
                    newList.remove(item)
                    submitList(newList)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRoutesViewHolder =
        FavoriteRoutesViewHolder(
            onItemClicked = onItemClicked,
            onFavIcClicked = onFavIcClicked,
            viewBinding = ItemRouteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FavoriteRoutesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}