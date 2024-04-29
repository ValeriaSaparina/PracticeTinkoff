package com.example.travels.di

import android.content.Context
import androidx.room.Room
import com.example.travels.data.places.local.FavoritesDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provide(@ApplicationContext ctx: Context): FavoritesDatabase =
        Room.databaseBuilder(ctx, FavoritesDatabase::class.java, "debug.db").build()

    @Provides
    @Singleton
    fun providePlacesDao(db: FavoritesDatabase) = db.favoritePlacesDao

}