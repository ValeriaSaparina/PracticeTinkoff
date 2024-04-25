package com.example.travels.domain.places.usecase

import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.domain.places.repository.PlacesRemoteRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetPlacesByQueryUseCase @Inject constructor(
    private val repository: PlacesRemoteRepository,
) {
    suspend operator fun invoke(query: String): Result<PlacesDomainModel> {
        return runSuspendCatching {
            repository.getPlaceByTextQuery(query)
        }
    }
}