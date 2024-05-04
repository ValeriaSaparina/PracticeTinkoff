package com.example.travels.domain.auth.usecase.validation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordValidationUseCase @Inject constructor() {
    operator fun invoke(password: String): Boolean {
        return password.length >= 8
    }
}