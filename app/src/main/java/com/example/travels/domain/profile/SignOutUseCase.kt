package com.example.travels.domain.profile

import com.example.travels.domain.auth.repositoty.UserRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return runSuspendCatching {
            repository.signOut()
        }
    }
}