package com.example.travels.ui.placeDetails.review.mapper

import com.example.travels.domain.review.model.ReviewDomainModel
import com.example.travels.ui.placeDetails.review.model.ReviewUiModel
import javax.inject.Inject

class ReviewUiModelMapper @Inject constructor() {
    fun toUiModel(review: ReviewDomainModel): ReviewUiModel {
        return with(review) {
            ReviewUiModel(
                author = user,
                rating = rating.toString(),
                text = text
            )
        }
    }

    fun toUiModel(reviews: List<ReviewDomainModel>?): List<ReviewUiModel> {
        return reviews?.map { toUiModel(it) } ?: listOf()
    }

}
