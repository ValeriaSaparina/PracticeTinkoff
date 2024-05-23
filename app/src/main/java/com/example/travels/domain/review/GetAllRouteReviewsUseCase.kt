package com.example.travels.domain.review

import com.example.travels.domain.review.model.UserReviewDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetAllRouteReviewsUseCase @Inject constructor(
    private val repository: RoutesRepository
) {
    suspend operator fun invoke(routeId: String): Result<List<UserReviewDomainModel>> {
        return runSuspendCatching {
            repository.getAllReviewsByRoute(routeId)
        }
    }
}
