package com.example.travels.domain.places.model

data class MetaDomainModel(
    val code: Int,
    val error: ErrorDomainModel?,
)

data class ErrorDomainModel(
    val type: String,
    val message: String
)
