package com.example.travels.ui.routes

import android.annotation.SuppressLint
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
        showData(item)
        setIcon(item.isFav)
        initListeners(item)
    }

    @SuppressLint("SetTextI18n")
    protected fun showData(item: RouteUIModel) {
        with(viewBinding) {
            routeNameTv.text = item.name
            valueRouteRateTv.text = item.rating.toString()
            valuerouteTypeTv.text = item.type
            valueAuthorTv.text = "${item.author.firstname} ${item.author.lastname}"
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