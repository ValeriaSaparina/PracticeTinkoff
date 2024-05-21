package com.example.travels.domain.places.usecase

import com.example.travels.data.review.ReviewModel
import com.example.travels.domain.auth.repositoty.UserRepository
import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.domain.review.model.ReviewDomainModel
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class SendPlaceReviewUseCase @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        placeId: Long,
        rating: String,
        text: String
    ): Result<ReviewDomainModel> {
        return runSuspendCatching {
            placesRepository.addReview(
                ReviewModel(
                    id = "",
                    rating = rating.toDouble(),
                    text = text,
                    userId = userRepository.getCurrentUserFromRemote().id,
                    placeId = placeId.toString()
                )
            )
        }
    }
}
