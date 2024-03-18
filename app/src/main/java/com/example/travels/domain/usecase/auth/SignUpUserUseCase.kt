package com.example.travels.domain.usecase.auth

import com.example.travels.domain.user.UserModel
import com.example.travels.domain.user.UserRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String, firstname: String, lastname: String, password: String
    ): Result<UserModel> {
        return runSuspendCatching {
            userRepository.signUp(email, firstname, lastname, password)
        }
    }
}