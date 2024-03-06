package com.example.travels.di

import com.example.travels.data.repository.FirebaseUserRepository
import com.example.travels.domain.auth.SignInUserUseCase
import com.example.travels.domain.auth.SignUpUserUseCase

object DataContainer {

    private val firebaseUserRepository = FirebaseUserRepository()

    val signUpUserUseCase = SignUpUserUseCase(firebaseUserRepository)
    val signInUserUseCase = SignInUserUseCase(firebaseUserRepository)

}