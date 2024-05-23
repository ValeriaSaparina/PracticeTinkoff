package com.example.travels.domain.routes.model

data class RouteDomainModel(
    val id: String,
    val name: String,
    val authorId: String,
    val type: String,
    val rating: Float,
    val isFav: Boolean
)
