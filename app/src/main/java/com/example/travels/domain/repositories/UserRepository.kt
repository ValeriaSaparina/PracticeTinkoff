package com.example.travels.domain.repositories

import com.example.travels.data.models.UserModel

interface UserRepository {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): UserModel?
    suspend fun saveUserToStore(user: UserModel): Boolean
}