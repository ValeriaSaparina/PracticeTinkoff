package com.example.travels.domain.places.usecase

import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class DeleteFromFavPlacesUseCase @Inject constructor(
    private val repository: PlacesRepository,
) {
    suspend operator fun invoke(id: Long) {
        runSuspendCatching { repository.deleteFromFavPlaces(id) }
    }
}