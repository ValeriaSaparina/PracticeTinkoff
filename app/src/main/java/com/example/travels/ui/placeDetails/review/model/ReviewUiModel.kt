package com.example.travels.ui.placeDetails.review.model

import com.example.travels.domain.auth.model.UserModel
import com.example.travels.ui.base.DisplayableItem

data class ReviewUiModel(
    val author: UserModel,
    val rating: String,
    val text: String
) : DisplayableItem
