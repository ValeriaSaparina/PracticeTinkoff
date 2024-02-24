package com.example.travels.data.models

data class UserModel(
    val id: String,
    val email: String,
    val firstname: String? = null,
    val lastname: String? = null,
)
