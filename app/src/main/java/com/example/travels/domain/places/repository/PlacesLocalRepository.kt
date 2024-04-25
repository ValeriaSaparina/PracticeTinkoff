package com.example.travels.domain.places.repository

import com.example.travels.domain.places.model.FavItemDomainModel

interface PlacesLocalRepository {
    suspend fun getAllFavPlaces(): List<FavItemDomainModel>

    suspend fun getFavPlaceById(id: Long): FavItemDomainModel

    suspend fun deleteAllFavPlaces()
    suspend fun addNewFavPlaces(vararg items: FavItemDomainModel)
    suspend fun deleteFromFavPlaces(item: FavItemDomainModel)
}