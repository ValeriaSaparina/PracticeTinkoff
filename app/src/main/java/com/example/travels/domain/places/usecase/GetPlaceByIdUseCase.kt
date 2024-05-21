package com.example.travels.domain.places.usecase

import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetPlaceByIdUseCase @Inject constructor(
    private val repository: PlacesRepository,
) {
    suspend operator fun invoke(id: Long): Result<PlaceDomainModel> {
        return runSuspendCatching {
            repository.getPlaceById(id)
        }
    }
}