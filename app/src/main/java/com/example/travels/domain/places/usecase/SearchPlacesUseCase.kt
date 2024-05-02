package com.example.travels.domain.places.usecase

import androidx.paging.PagingData
import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPlacesUseCase @Inject constructor(
    private val repository: PlacesRepository,
) {
    operator fun invoke(query: String): Flow<PagingData<PlaceDomainModel>> {
        return repository.searchPlaces(query)
    }
}
