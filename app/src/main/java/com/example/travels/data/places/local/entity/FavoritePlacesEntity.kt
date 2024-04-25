package com.example.travels.data.places.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_places")
data class FavoritePlacesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val type: String,
    val name: String,
    val description: String,
    val address: String,
    val review: Float,
)