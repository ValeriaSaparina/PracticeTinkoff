package com.example.travels.domain.places.usecase

import androidx.paging.PagingData
import com.example.travels.domain.places.repository.PlacesRemoteRepository
import com.example.travels.ui.places.model.ItemUiModel
import com.example.travels.utils.runSuspendCatching
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPlacesUseCase @Inject constructor(
    private val repository: PlacesRemoteRepository,
) {
    suspend operator fun invoke(query: String): Result<Flow<PagingData<ItemUiModel>>> {
        return runSuspendCatching {
            repository.searchPlaces(query)
        }
    }
}