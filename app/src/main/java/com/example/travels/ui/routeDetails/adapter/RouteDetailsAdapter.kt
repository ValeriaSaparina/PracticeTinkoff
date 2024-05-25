package com.example.travels.ui.routeDetails.adapter

import android.annotation.SuppressLint
import com.example.travels.R
import com.example.travels.databinding.ItemRouteDetailsBinding
import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.routes.model.RouteUIModel
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

@SuppressLint("SetTextI18n")
fun placeDetailsAdapterDelegate(sendReview: (String, String) -> Unit) =
    adapterDelegateViewBinding<RouteUIModel, DisplayableItem, ItemRouteDetailsBinding>(
        { layoutInflater, root ->
            ItemRouteDetailsBinding.inflate(layoutInflater, root, false)
        }
    ) {
        bind {
            with(binding) {
                with(details) {
                    with(item) {
                        nameTv.text = name
                        typeTv.text = type
                        authorTv.text = "${author.firstname} ${author.lastname}"
                        ratingTv.text = rating.toString()
                    }
                    favIc.setImageResource(setIcon(item))
                }
                reviewBtn.setOnClickListener {
                    sendReview(ratingEt.text.toString(), reviewEt.text.toString())
                }
            }
        }
    }

private fun setIcon(item: RouteUIModel): Int {
    return if (item.isFav) R.drawable.outline_favorite_24 else R.drawable.outline_favorite_border_24
}