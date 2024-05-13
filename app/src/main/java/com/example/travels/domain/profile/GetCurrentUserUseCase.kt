package com.example.travels.domain.profile

import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.auth.repositoty.UserRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserModel?> {
        return runSuspendCatching {
            userRepository.getCurrentUserFromLocal()
        }
    }
}