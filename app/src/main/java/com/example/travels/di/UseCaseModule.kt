package com.example.travels.di

import com.example.travels.domain.usecase.validation.EmailValidationUseCase
import com.example.travels.domain.usecase.validation.NameValidationUseCase
import com.example.travels.domain.usecase.validation.PasswordValidationUseCase
import com.example.travels.domain.usecase.validation.SamePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideEmailValidationUseCase(): EmailValidationUseCase {
        return EmailValidationUseCase()
    }


    @Provides
    @Singleton
    fun provideNameValidationUseCase(): NameValidationUseCase {
        return NameValidationUseCase()
    }


    @Provides
    @Singleton
    fun providePasswordValidationUseCase(): PasswordValidationUseCase {
        return PasswordValidationUseCase()
    }


    @Provides
    @Singleton
    fun provideSamePasswordUseCase(): SamePasswordUseCase {
        return SamePasswordUseCase()
    }

}