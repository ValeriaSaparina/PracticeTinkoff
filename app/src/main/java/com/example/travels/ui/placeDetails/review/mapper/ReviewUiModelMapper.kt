package com.example.travels.ui.placeDetails.review.mapper

import com.example.travels.domain.review.model.UserReviewDomainModel
import com.example.travels.ui.placeDetails.review.model.ReviewUiModel
import javax.inject.Inject

class ReviewUiModelMapper @Inject constructor() {
    fun toUiModel(review: UserReviewDomainModel): ReviewUiModel {
        return with(review) {
            ReviewUiModel(
                author = user,
                rating = rating.toString(),
                text = text
            )
        }
    }

    fun toUiModel(reviews: List<UserReviewDomainModel>?): List<ReviewUiModel> {
        return reviews?.map { toUiModel(it) } ?: listOf()
    }

}
