package com.example.travels.domain.places.repository

import com.example.travels.domain.places.model.PlacesDomainModel

interface PlacesRepository {

    suspend fun getPlaceByTextQuery(query: String): PlacesDomainModel

}