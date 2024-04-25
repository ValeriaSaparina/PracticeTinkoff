package com.example.travels.domain.places.usecase

import com.example.travels.domain.places.repository.PlacesLocalRepository
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.ItemUiModel
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class AddNewFavPlaceUseCase @Inject constructor(
    private val repository: PlacesLocalRepository,
    private val mapper: PlacesUiModelMapper
) {

    suspend operator fun invoke(item: ItemUiModel) {
        runSuspendCatching {
            repository.addNewFavPlaces(mapper.toFavItemDomainModel(item))
        }
    }

}