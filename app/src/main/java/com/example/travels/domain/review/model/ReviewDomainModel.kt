package com.example.travels.domain.review.model

import com.example.travels.domain.auth.model.UserModel

data class ReviewDomainModel(
    val id: String,
    val rating: Double,
    val text: String,
    val user: UserModel,
    val placeId: Long
)
