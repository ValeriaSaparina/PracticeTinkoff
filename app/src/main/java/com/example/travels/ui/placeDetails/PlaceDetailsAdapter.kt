package com.example.travels.ui.placeDetails

import com.example.travels.R
import com.example.travels.databinding.ItemDetailsBinding
import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.places.model.PlaceUiModel
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun placeDetailsAdapterDelegate(sendReview: (String, String) -> Unit) =
    adapterDelegateViewBinding<PlaceUiModel, DisplayableItem, ItemDetailsBinding>(
        { layoutInflater, root ->
            ItemDetailsBinding.inflate(layoutInflater, root, false)
        }
    ) {
        bind {
            with(binding) {
                with(details) {
                    with(item) {
                        nameTv.text = name
                        descriptionTv.text = description
                        addressTv.text = address
                        ratingTv.text = review.rating.toString()
                        favIc.setImageResource(setIcon(item))
                    }
                }

                reviewBtn.setOnClickListener {
                    sendReview(ratingEt.text.toString(), reviewEt.text.toString())
                }
            }
        }
    }

private fun setIcon(item: PlaceUiModel): Int {
    return if (item.isFav) R.drawable.outline_favorite_24 else R.drawable.outline_favorite_border_24
}
