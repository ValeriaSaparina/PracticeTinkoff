package com.example.travels.data.review.mapper

import com.example.travels.data.review.ReviewModel
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.review.model.UserReviewDomainModel
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserReviewDomainModelMapper @Inject constructor() {
    fun firebaseDocToReviewModel(document: DocumentSnapshot?): ReviewModel {
        return document?.run {
            ReviewModel(
                id = id,
                rating = getDouble("rating") ?: 0.0,
                text = getString("text") ?: "",
                userId = getString("user_id") ?: "",
                routeId = getString("place_id") ?: "",
            )
        } ?: ReviewModel(
            id = "",
            rating = 0.0,
            text = "",
            userId = "",
            routeId = "",
        )
    }

    fun toDomainModel(review: DocumentSnapshot?, user: UserModel): UserReviewDomainModel {
        return with(firebaseDocToReviewModel(review)) {
            UserReviewDomainModel(
                id = id,
                rating = rating,
                text = text,
                user = user,
                routeId = routeId
            )
        }
    }
}
