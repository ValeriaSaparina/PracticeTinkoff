package com.example.travels.ui.routeDetails.adapter

import android.annotation.SuppressLint
import com.example.travels.databinding.ItemReviewBinding
import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.routeDetails.review.model.UserReviewUiModel
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

@SuppressLint("SetTextI18n")
fun reviewsAdapterDelegate() =
    adapterDelegateViewBinding<UserReviewUiModel, DisplayableItem, ItemReviewBinding>(
        { layoutInflater, root ->
            ItemReviewBinding.inflate(layoutInflater, root, false)
        }
    ) {
        bind {
            with(binding) {
                with(item) {
                    nameTv.text = "${author.firstname} ${author.lastname}"
                    ratingTv.text = rating
                    textTv.text = text
                }
            }
        }
    }