package com.example.travels.ui.routeDetails.adapter

import com.example.travels.databinding.ItemRouteDetailsBinding
import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.routes.model.RouteUIModel
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

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
                        authorTv.text = author
                        ratingTv.text = rating.toString()
                    }
                }

                reviewBtn.setOnClickListener {
                    sendReview(ratingEt.text.toString(), reviewEt.text.toString())
                }
            }
        }
    }