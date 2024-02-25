package com.example.travels.domain.user

interface UserRepository {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): UserModel
    suspend fun saveUserToStore(user: UserModel): Boolean
    suspend fun signUp(email: String, firstname: String, lastname: String, password: String): UserModel
    suspend fun signIn(email: String, password: String) : Boolean
    suspend fun getCurrentUser(): UserModel
    suspend fun initCurrentUser()
}