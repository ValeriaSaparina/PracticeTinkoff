package com.example.travels.data.routes.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travels.data.routes.entity.FavoriteRouteEntity

@Dao
interface FavoriteRoutesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewRoute(route: FavoriteRouteEntity)

    @Query("select * from favorite_routes")
    suspend fun getAllFavRoutes(): List<FavoriteRouteEntity>?

    @Query("select * from favorite_routes where id=:id")
    suspend fun getFavRoute(id: String): FavoriteRouteEntity?

    @Query("delete from favorite_routes where id=:id")
    suspend fun deleteFavRoute(id: String)

    @Query("delete from favorite_routes")
    suspend fun deleteAllFavRoutes()

    @Query("select id from favorite_routes")
    suspend fun getIdAllFavRoutes(): List<String>
}

