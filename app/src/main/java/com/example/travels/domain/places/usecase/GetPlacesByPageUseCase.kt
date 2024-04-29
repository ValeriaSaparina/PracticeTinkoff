package com.example.travels.domain.places.usecase

import androidx.paging.PagingData
import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.utils.runSuspendCatching
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPlacesUseCase @Inject constructor(
    private val repository: PlacesRepository,
) {
    suspend operator fun invoke(query: String): Result<Flow<PagingData<PlaceDomainModel>>> {
        return runSuspendCatching {
            repository.searchPlaces(query)
        }
    }
}