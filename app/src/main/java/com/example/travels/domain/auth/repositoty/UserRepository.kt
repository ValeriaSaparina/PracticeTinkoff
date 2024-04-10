package com.example.travels.domain.auth.repositoty

import com.example.travels.domain.auth.model.UserModel

interface UserRepository {
    suspend fun signUp(
        email: String,
        firstname: String,
        lastname: String,
        password: String
    ): UserModel

    suspend fun signIn(email: String, password: String): UserModel
    suspend fun getUserById(uId: String): UserModel
}