package com.example.travels.domain.usecase.auth

import com.example.travels.domain.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        userRepository.signIn(email, password)
    }
}