package com.example.travels.domain.auth

import com.example.travels.domain.user.UserRepository

class SignInUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Boolean =
        userRepository.signIn(email, password)
}