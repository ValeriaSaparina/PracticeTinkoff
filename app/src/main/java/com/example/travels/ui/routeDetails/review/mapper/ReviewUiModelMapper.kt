package com.example.travels.ui.routeDetails.review.mapper

import com.example.travels.domain.review.model.UserReviewDomainModel
import com.example.travels.ui.routeDetails.review.model.UserReviewUiModel
import javax.inject.Inject

class ReviewUiModelMapper @Inject constructor() {
    fun toUiModel(review: UserReviewDomainModel): UserReviewUiModel {
        return with(review) {
            UserReviewUiModel(
                author = user,
                rating = rating.toString(),
                text = text
            )
        }
    }

    fun toUiModel(reviews: List<UserReviewDomainModel>?): List<UserReviewUiModel> {
        return reviews?.map { toUiModel(it) } ?: listOf()
    }

}
