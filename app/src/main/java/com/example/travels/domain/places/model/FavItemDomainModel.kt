package com.example.travels.domain.places.model

data class FavItemDomainModel(
    val id: Long,
    val type: String,
    val name: String,
    val description: String,
    val address: String,
    val review: Float,
)