package com.example.travels.domain.usecase.auth

import com.example.travels.domain.user.UserModel
import com.example.travels.domain.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String, firstname: String, lastname: String, password: String
    ): UserModel = userRepository.signUp(email, firstname, lastname, password)
}