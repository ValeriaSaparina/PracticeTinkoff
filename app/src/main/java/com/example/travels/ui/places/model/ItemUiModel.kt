package com.example.travels.ui.places.model

data class ItemUiModel(
    val id: String = "",
    val type: String = "",
    val name: String = "",
    val description: String = "",
    val address: String = "",
    val review: ReviewUiModel = ReviewUiModel(),
)

data class ReviewUiModel(
    val rating: Float = -1f,
)
