package com.example.travels.domain.places.repository

import androidx.paging.PagingData
import com.example.travels.domain.places.model.FavItemDomainModel
import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.model.PlacesDomainModel
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    suspend fun getPlaceByTextQuery(query: String): PlacesDomainModel
    suspend fun getPlacesByPage(query: String, page: Long, pageSize: Int = 50): PlacesDomainModel
    fun searchPlaces(query: String): Flow<PagingData<PlaceDomainModel>>

    suspend fun getAllFavPlaces(): List<FavItemDomainModel>
    suspend fun getIdAllFavPlaces(): List<Long>
    suspend fun getFavPlaceById(id: Long): FavItemDomainModel
    suspend fun deleteAllFavPlaces()
    suspend fun deleteFromFavPlaces(id: Long)
    suspend fun addNewFavPlaces(vararg items: FavItemDomainModel)

    suspend fun getFavPlaces(n: Int): List<FavItemDomainModel>
}
