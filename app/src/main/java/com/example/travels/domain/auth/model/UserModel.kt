package com.example.travels.domain.auth.model

data class UserModel(
    val id: String,
    val email: String,
    val firstname: String? = null,
    val lastname: String? = null,
)
