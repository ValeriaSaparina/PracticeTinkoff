package com.example.travels.domain.places.repository

import androidx.paging.PagingData
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.ui.places.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    suspend fun getPlaceByTextQuery(query: String): PlacesDomainModel
    suspend fun getPlacesByPage(query: String, page: Long, pageSize: Int = 50): PlacesDomainModel
    suspend fun searchPlaces(query: String = "Kazan"): Flow<PagingData<ItemUiModel>>

}