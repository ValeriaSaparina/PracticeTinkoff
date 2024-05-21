package com.example.travels.data.routes.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_routes")
data class FavoriteRouteEntity(
    @PrimaryKey val id: String,
    val name: String,
    val authorId: String,
    val type: String,
    val noteId: Long
)

@Entity(tableName = "authors")
data class AuthorEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: Long,
    val text: String
)
