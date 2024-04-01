package com.example.travels.di

import com.example.travels.data.places.repository.PlacesRepositoryImpl
import com.example.travels.data.repository.FirebaseUserRepository
import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.domain.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BinderModule {

    @Binds
    @Singleton
    fun bindUserRepositoryImpl(userRepositoryImpl: FirebaseUserRepository): UserRepository

    @Binds
    @Singleton
    fun bindPlacesRepositoryImpl(placesRepositoryImpl: PlacesRepositoryImpl): PlacesRepository

}