package com.example.travels.domain.places.model

data class ItemDomainModel(
    val id: String,
    val type: String,
    val name: String,
    val addressName: String,
    val addressComment: String,
    val review: ReviewDomainModel,
)

data class ReviewDomainModel(
    val rating: Float,
)
