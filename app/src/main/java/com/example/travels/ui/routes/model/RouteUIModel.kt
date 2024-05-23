package com.example.travels.ui.routes.model

import com.example.travels.ui.base.DisplayableItem

data class RouteUIModel(
    val id: String,
    val name: String,
    val author: String,
    val type: String,
    val rating: Float,
    var isFav: Boolean
) : DisplayableItem
