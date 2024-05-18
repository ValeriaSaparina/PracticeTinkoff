package com.example.travels.domain.places.usecase

import com.example.travels.domain.places.model.FavItemDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetFavoritePlacesUseCase @Inject constructor(
    private val repository: PlacesRepository,
) {
    suspend operator fun invoke(n: Int? = null): Result<List<FavItemDomainModel>> {
        return runSuspendCatching {
            if (n == null) {
                repository.getAllFavPlaces()
            } else {
                repository.getFavPlaces(n)
            }
        }
    }
}