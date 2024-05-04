package com.example.travels.domain.places.model

data class PlaceDomainModel(
    val id: String,
    val type: String,
    val name: String,
    val addressName: String,
    val addressComment: String,
    val review: ReviewDomainModel,
    val isFav: Boolean
)

data class ReviewDomainModel(
    val rating: Float,
)
