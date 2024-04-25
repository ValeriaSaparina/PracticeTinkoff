package com.example.travels.di

import com.example.travels.data.places.local.repository.PlacesLocalRepositoryImpl
import com.example.travels.data.places.remote.repository.PlacesRemoteRepositoryImpl
import com.example.travels.data.user.repository.FirebaseUserRepository
import com.example.travels.domain.auth.repositoty.UserRepository
import com.example.travels.domain.places.repository.PlacesLocalRepository
import com.example.travels.domain.places.repository.PlacesRemoteRepository
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
    fun bindPlacesRemoteRepositoryImpl(placesRepositoryImpl: PlacesRemoteRepositoryImpl): PlacesRemoteRepository

    @Binds
    @Singleton
    fun bindPlacesLocalRepositoryImpl(placesLocalRepositoryImpl: PlacesLocalRepositoryImpl): PlacesLocalRepository
}