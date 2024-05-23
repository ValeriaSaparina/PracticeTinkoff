package com.example.travels.ui.routeDetails.review.model

import com.example.travels.domain.auth.model.UserModel
import com.example.travels.ui.base.DisplayableItem

data class UserReviewUiModel(
    val author: UserModel,
    val rating: String,
    val text: String
) : DisplayableItem
