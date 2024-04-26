package com.example.travels.data.places.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travels.data.places.local.entity.FavoritePlacesEntity

@Dao
interface FavoritePlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewPlaces(vararg places: FavoritePlacesEntity)

    @Query("select * from favorite_places")
    suspend fun getAllFavPlaces(): List<FavoritePlacesEntity>?

    @Query("select * from favorite_places where id=:id")
    suspend fun getFavPlace(id: Long): FavoritePlacesEntity?

    @Delete
    suspend fun deleteFavPlace(place: FavoritePlacesEntity)

    @Query("DELETE FROM favorite_places")
    suspend fun deleteAllFavPlaces()
}