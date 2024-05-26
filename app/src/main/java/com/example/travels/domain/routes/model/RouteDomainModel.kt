package com.example.travels.domain.routes.model

import com.example.travels.domain.auth.model.UserModel

data class RouteDomainModel(
    val id: String,
    val name: String,
    val author: UserModel,
    val type: String,
    val rating: Float,
    val placesId: List<String>,
    val isFav: Boolean
)
