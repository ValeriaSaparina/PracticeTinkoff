package com.example.travels.data.places.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.travels.data.places.local.dao.FavoritePlacesDao
import com.example.travels.data.places.local.entity.FavoritePlacesEntity
import com.example.travels.data.routes.dao.FavoriteRoutesDao
import com.example.travels.data.routes.entity.FavoriteRouteEntity

@Database(
    entities = [
        FavoritePlacesEntity::class,
        FavoriteRouteEntity::class
    ],
    version = 1
)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract val favoritePlacesDao: FavoritePlacesDao
    abstract val favoriteRoutesDao: FavoriteRoutesDao

}