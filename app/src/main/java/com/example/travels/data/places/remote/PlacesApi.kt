package com.example.travels.data.places.remote

import com.example.travels.data.places.remote.response.PlacesResponseModel
import com.example.travels.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("items")
    suspend fun getPlaceByTextQuery(
        @Query("q") query: String,
        @Query("page_size") pageSize: Int = Constants.MAX_PAGE_SIZE,
    ): PlacesResponseModel?

    @GET("items")
    suspend fun getPlacesByQueryPage(
        @Query("q") query: String,
        @Query("page_size") pageSize: Int = Constants.MAX_PAGE_SIZE,
        @Query("page") page: Int
    ): PlacesResponseModel?

}