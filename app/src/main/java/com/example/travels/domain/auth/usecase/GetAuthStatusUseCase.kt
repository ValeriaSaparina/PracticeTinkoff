package com.example.travels.domain.auth.usecase

import com.example.travels.domain.auth.repositoty.UserRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject


class GetAuthStatusUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): Result<Boolean> {
        return runSuspendCatching {
            repository.isAuth()
        }
    }

}
