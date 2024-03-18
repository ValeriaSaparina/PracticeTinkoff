package com.example.travels.domain.usecase.validation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SamePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}