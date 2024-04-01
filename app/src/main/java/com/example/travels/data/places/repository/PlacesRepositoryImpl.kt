package com.example.travels.data.places.repository

import com.example.travels.data.places.mapper.PlacesDomainModelMapper
import com.example.travels.data.places.remote.PlacesApi
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesApi: PlacesApi,
    private val mapper: PlacesDomainModelMapper,
) : PlacesRepository {

    override suspend fun getPlaceByTextQuery(query: String): PlacesDomainModel {
        return mapper.mapResponseToDomainModel(response = placesApi.getPlaceByTextQuery(query))
            ?: throw NullPointerException("response is null")
    }

}