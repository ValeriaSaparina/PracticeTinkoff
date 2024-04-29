package com.example.travels.data.places.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.travels.data.places.local.dao.FavoritePlacesDao
import com.example.travels.data.places.local.entity.FavoritePlacesEntity

@Database(
    entities = [
        FavoritePlacesEntity::class,
    ],
    version = 1
)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract val favoritePlacesDao: FavoritePlacesDao

}