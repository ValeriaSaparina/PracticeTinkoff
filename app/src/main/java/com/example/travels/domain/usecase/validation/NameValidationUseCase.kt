package com.example.travels.domain.usecase.validation

import com.example.travels.utils.Regexes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NameValidationUseCase @Inject constructor() {
    operator fun invoke(name: String): Boolean = name.matches(Regexes.nameRegex)
}