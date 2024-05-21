package com.example.travels.domain.places.usecase

import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.domain.review.model.ReviewDomainModel
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetAllReviewsUseCase @Inject constructor(
    private val repository: PlacesRepository,
) {
    suspend operator fun invoke(placeId: Long): Result<List<ReviewDomainModel>> {
        return runSuspendCatching {
            repository.getAllReviewsByPlace(placeId)
        }
    }
}
