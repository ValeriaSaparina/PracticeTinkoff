package com.example.travels.data.routes.model

import com.example.travels.domain.auth.model.UserModel

data class RouteDataModel(
    val id: String,
    val name: String,
    val rating: Float,
    val author: UserModel,
    val isFav: Boolean,
    val type: String
)