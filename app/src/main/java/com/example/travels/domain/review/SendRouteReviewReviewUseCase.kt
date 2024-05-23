package com.example.travels.domain.review

import com.example.travels.data.review.ReviewModel
import com.example.travels.domain.auth.repositoty.UserRepository
import com.example.travels.domain.review.model.UserReviewDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class SendRouteReviewReviewUseCase @Inject constructor(
    private val routesRepository: RoutesRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        routeId: String,
        rating: String,
        text: String
    ): Result<UserReviewDomainModel> {
        return runSuspendCatching {
            routesRepository.addReview(
                ReviewModel(
                    id = "",
                    rating = rating.toDouble(),
                    text = text,
                    userId = userRepository.getCurrentUserFromRemote().id,
                    routeId = routeId
                )
            )
        }
    }
}
