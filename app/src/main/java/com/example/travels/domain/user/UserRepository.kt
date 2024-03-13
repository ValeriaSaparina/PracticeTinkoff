package com.example.travels.domain.user

interface UserRepository {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): UserModel
    suspend fun saveUserToStore(user: UserModel)
    suspend fun signUp(email: String, firstname: String, lastname: String, password: String): UserModel
    suspend fun signIn(email: String, password: String)
    suspend fun getCurrentUser(): UserModel
    suspend fun initCurrentUser()
}