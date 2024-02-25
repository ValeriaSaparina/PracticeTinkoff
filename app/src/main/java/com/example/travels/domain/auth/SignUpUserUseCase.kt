package com.example.travels.domain.auth

import com.example.travels.domain.user.UserModel
import com.example.travels.domain.user.UserRepository

class SignUpUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String, firstname: String, lastname: String, password:String
    ): UserModel = userRepository.signUp(email, firstname, lastname, password)
}