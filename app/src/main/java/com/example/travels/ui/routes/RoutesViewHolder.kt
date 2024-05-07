package com.example.travels.ui.routes

import androidx.recyclerview.widget.RecyclerView
import com.example.travels.R
import com.example.travels.databinding.ItemRouteBinding
import com.example.travels.ui.routes.model.RouteUIModel

open class RoutesViewHolder(
    private val viewBinding: ItemRouteBinding,
    private val onFavIcClicked: (RouteUIModel) -> Unit,
    private val onItemClicked: (RouteUIModel) -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

    open fun bind(item: RouteUIModel) {
        with(viewBinding) {
            showData(item)
            routeNameTv.text = item.name
//                routeRateTv.text = item.rate
//                routeTypeTv.text = item.type
//                authorTv.text = item.author
            setIcon(item.isFav)
            initListeners(item)
        }
    }

    protected fun showData(item: RouteUIModel) {
        with(viewBinding) {
            routeNameTv.text = item.name
//                routeRateTv.text = item.rate
//                routeTypeTv.text = item.type
//                authorTv.text = item.author}
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