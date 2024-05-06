package com.example.travels.domain.places.usecase

import com.example.travels.domain.places.model.FavItemDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetFavoritePlacesUseCase @Inject constructor(
    private val repository: PlacesRepository,
) {
    suspend operator fun invoke(): Result<List<FavItemDomainModel>> {
        return runSuspendCatching {
            repository.getAllFavPlaces()
        }
    }
}