package com.example.travels.ui.places.model

import com.example.travels.ui.base.DisplayableItem

data class PlaceUiModel(
    val id: String,
    val type: String,
    val name: String,
    val description: String,
    val address: String,
    val review: ReviewUiModel,
    var isFav: Boolean
) : DisplayableItem

data class ReviewUiModel(
    val rating: Float,
)
