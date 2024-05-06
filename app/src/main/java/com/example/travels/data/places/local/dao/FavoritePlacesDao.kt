package com.example.travels.data.places.local.dao

import androidx.room.Dao
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

    @Query("select * from favorite_places limit :n")
    suspend fun getFavPlaces(n: Int): List<FavoritePlacesEntity>?

    @Query("select * from favorite_places where id=:id")
    suspend fun getFavPlace(id: Long): FavoritePlacesEntity?

    @Query("delete from favorite_places where id=:id")
    suspend fun deleteFavPlace(id: Long)

    @Query("delete from favorite_places")
    suspend fun deleteAllFavPlaces()

    @Query("select id from favorite_places")
    suspend fun getIdAllFavPlaces(): List<Long>
}