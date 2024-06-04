package com.example.travels.ui.routes.model

data class RouteUIModel(
    val id: String,
    val name: String,
    val authorId: String,
    val type: String,
    var isFav: Boolean
)
