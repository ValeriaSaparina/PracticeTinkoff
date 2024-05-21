package com.example.travels.data.review.mapper

import com.example.travels.data.review.ReviewModel
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.review.model.ReviewDomainModel
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewDomainModelMapper @Inject constructor() {
    fun firebaseDocToReviewModel(document: DocumentSnapshot?): ReviewModel {
        return document?.run {
            ReviewModel(
                id = id,
                rating = getDouble("rating") ?: 0.0,
                text = getString("text") ?: "",
                userId = getString("user_id") ?: "",
                placeId = getString("place_id") ?: "",
            )
        } ?: ReviewModel(
            id = "",
            rating = 0.0,
            text = "",
            userId = "",
            placeId = "",
        )
    }

    fun toDomainModel(review: DocumentSnapshot?, user: UserModel): ReviewDomainModel {
        return with(firebaseDocToReviewModel(review)) {
            ReviewDomainModel(
                id = id,
                rating = rating,
                text = text,
                user = user,
                placeId = placeId.toLong()
            )
        }
    }
}
