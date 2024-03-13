package com.example.travels.domain.usecase.validation




class PasswordValidationUseCase {
    operator fun invoke(password: String): Boolean {
        return password.length >= 8
    }
}