package com.example.travels.data.places.remote

import com.example.travels.data.places.remote.response.PlacesResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("items")
    suspend fun getPlaceByTextQuery(
        @Query("q") query: String
    ): PlacesResponseModel?

}