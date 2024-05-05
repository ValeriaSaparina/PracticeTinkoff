package com.example.travels.ui.routes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travels.R
import com.example.travels.databinding.ItemRouteBinding
import com.example.travels.ui.routes.model.RouteUIModel

class RoutesAdapter(
    private val onFavIcClicked: (RouteUIModel) -> Unit,
    private val onItemClicked: (RouteUIModel) -> Unit,
) : ListAdapter<RouteUIModel, RoutesAdapter.ViewHolder>(RoutesDiffCallback()) {
    inner class ViewHolder(
        private val viewBinding: ItemRouteBinding,
        private val onFavIcClicked: (RouteUIModel) -> Unit,
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: RouteUIModel) {
            with(viewBinding) {
                routeNameTv.text = item.name
//                routeRateTv.text = item.rate
//                routeTypeTv.text = item.type
//                authorTv.text = item.author
                setIcon(item.isFav)
                initListeners(item)
            }
        }

        private fun initListeners(item: RouteUIModel) {
            with(viewBinding) {
                favIc.setOnClickListener {
                    onFavIcClicked(item)
                    setIcon(!item.isFav)
                }
                root.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }

        private fun setIcon(isFav: Boolean) {
            val ic =
                if (isFav) R.drawable.outline_favorite_24 else R.drawable.outline_favorite_border_24
            viewBinding.favIc.setBackgroundResource(ic)
        }

    }

    private class RoutesDiffCallback : DiffUtil.ItemCallback<RouteUIModel>() {
        override fun areItemsTheSame(oldItem: RouteUIModel, newItem: RouteUIModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RouteUIModel, newItem: RouteUIModel): Boolean =
            oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutesAdapter.ViewHolder =
        ViewHolder(
            viewBinding = ItemRouteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onFavIcClicked = onFavIcClicked
        )

    override fun onBindViewHolder(holder: RoutesAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}