package com.example.travels.data.review

data class ReviewModel(
    val id: String,
    val rating: Double,
    val text: String,
    val userId: String,
    val placeId: String,
)
