package com.example.travels.domain.places.repository

import androidx.paging.PagingData
import com.example.travels.domain.places.model.FavItemDomainModel
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.ui.places.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    suspend fun getPlaceByTextQuery(query: String): PlacesDomainModel
    suspend fun getPlacesByPage(query: String, page: Long, pageSize: Int = 50): PlacesDomainModel
    suspend fun searchPlaces(query: String = "Cafe"): Flow<PagingData<ItemUiModel>>

    suspend fun getAllFavPlaces(): List<FavItemDomainModel>
    suspend fun getIdAllFavPlaces(): List<Long>
    suspend fun getFavPlaceById(id: Long): FavItemDomainModel
    suspend fun deleteAllFavPlaces()
    suspend fun deleteFromFavPlaces(item: FavItemDomainModel)
    suspend fun addNewFavPlaces(vararg items: FavItemDomainModel)

}