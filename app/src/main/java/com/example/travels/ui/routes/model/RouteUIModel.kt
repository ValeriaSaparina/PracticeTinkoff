package com.example.travels.ui.routes.model

import com.example.travels.domain.auth.model.UserModel
import com.example.travels.ui.base.DisplayableItem

data class RouteUIModel(
    val id: String,
    val name: String,
    val author: UserModel,
    val type: String,
    val rating: Float,
    var isFav: Boolean
) : DisplayableItem
