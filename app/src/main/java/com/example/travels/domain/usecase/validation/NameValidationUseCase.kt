package com.example.travels.domain.usecase.validation

import com.example.travels.utils.Regexes

class NameValidationUseCase {
    operator fun invoke(name: String): Boolean = name.matches(Regexes.nameRegex)
}