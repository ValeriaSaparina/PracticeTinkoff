package com.example.travels.domain.places.model

data class ResultDomainModel(
    val items: List<ItemDomainModel?>,
    val total: Int
)
