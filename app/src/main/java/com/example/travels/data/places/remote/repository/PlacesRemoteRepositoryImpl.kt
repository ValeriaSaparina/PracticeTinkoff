package com.example.travels.data.places.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.travels.data.places.PlacesPagingSource
import com.example.travels.data.places.remote.PlacesApi
import com.example.travels.data.places.remote.mapper.PlacesResponseDomainModelMapper
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.domain.places.repository.PlacesRemoteRepository
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.ItemUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlacesRemoteRepositoryImpl @Inject constructor(
    private val placesApi: PlacesApi,
    private val mapper: PlacesResponseDomainModelMapper,
    private val uiModelMapper: PlacesUiModelMapper
) : PlacesRemoteRepository {

    override suspend fun getPlaceByTextQuery(query: String): PlacesDomainModel {
        return mapper.mapResponseToDomainModel(response = placesApi.getPlaceByTextQuery(query))
    }

    override suspend fun getPlacesByPage(
        query: String,
        page: Long,
        pageSize: Int
    ): PlacesDomainModel {
        return mapper.mapResponseToDomainModel(
            placesApi.getPlacesByQueryPage(
                query = query,
                page = page.toInt()
            )
        )
    }

    override suspend fun searchPlaces(query: String): Flow<PagingData<ItemUiModel>> = Pager(
        pagingSourceFactory = {
            PlacesPagingSource(
                placesApi,
                mapperDomainModel = mapper,
                mapperUiModel = uiModelMapper,
                query = query
            )
        },
        config = PagingConfig(
            pageSize = 10
        )
    ).flow


}