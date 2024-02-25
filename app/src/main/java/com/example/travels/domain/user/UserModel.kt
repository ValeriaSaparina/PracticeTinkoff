package com.example.travels.domain.user

data class UserModel(
    val id: String,
    val email: String,
    val firstname: String? = null,
    val lastname: String? = null,
) {
    constructor() : this("", "")
}
