package com.example.travels.domain.usecase.validation

class SamePasswordUseCase {
    operator fun invoke(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}