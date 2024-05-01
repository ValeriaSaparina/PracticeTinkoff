package com.example.travels.domain.profile

import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.auth.repositoty.UserRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(id: String): Result<UserModel> {
        return runSuspendCatching {
            repository.getUserById(id)
        }
    }
}