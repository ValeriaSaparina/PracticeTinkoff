package com.example.travels.domain.usecase.validation

import android.util.Patterns

class EmailValidationUseCase {
    operator fun invoke(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}