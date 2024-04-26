package com.example.travels.ui.places.model

data class PlaceUiModel(
    val id: String,
    val type: String,
    val name: String,
    val description: String,
    val address: String,
    val review: ReviewUiModel,
    var isFav: Boolean
)

data class ReviewUiModel(
    val rating: Float,
)
