package com.example.travels.data.places.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.travels.data.places.PlacesPagingSource
import com.example.travels.data.places.local.dao.FavoritePlacesDao
import com.example.travels.data.places.local.entity.FavoritePlacesEntity
import com.example.travels.data.places.local.mapper.FavPlaceDomainModelMapper
import com.example.travels.data.places.remote.PlacesApi
import com.example.travels.data.places.remote.mapper.PlacesResponseDomainModelMapper
import com.example.travels.domain.places.model.FavItemDomainModel
import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesApi: PlacesApi,
    private val favoritePlacesDao: FavoritePlacesDao,
    private val favPlaceDomainModelMapper: FavPlaceDomainModelMapper,
    private val responseDomainModelMapper: PlacesResponseDomainModelMapper,
) : PlacesRepository {

    override suspend fun getPlaceByTextQuery(query: String): PlacesDomainModel {
        return responseDomainModelMapper.mapResponseToDomainModel(
            response = placesApi.getPlaceByTextQuery(
                query
            )
        )
    }

    override suspend fun getPlacesByPage(
        query: String,
        page: Long,
        pageSize: Int
    ): PlacesDomainModel {
        return responseDomainModelMapper.mapResponseToDomainModel(
            placesApi.getPlacesByQueryPage(
                query = query,
                page = page.toInt()
            )
        )
    }

    override fun searchPlaces(query: String): Flow<PagingData<PlaceDomainModel>> = Pager(
        pagingSourceFactory = {
            PlacesPagingSource(
                placesApi = placesApi,
                repository = this,
                mapperDomainModel = responseDomainModelMapper,
                query = query
            )
        },
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 4,
        )
    ).flow

    override suspend fun addNewFavPlaces(vararg items: FavItemDomainModel) {
        val entities = mutableListOf<FavoritePlacesEntity>()
        items.forEach {
            entities.add(favPlaceDomainModelMapper.toEntity(it))
        }
        favoritePlacesDao.addNewPlaces(*entities.toTypedArray())
    }

    override suspend fun deleteFromFavPlaces(id: Long) {
        favoritePlacesDao.deleteFavPlace(id)
    }

    override suspend fun getAllFavPlaces(): List<FavItemDomainModel> {
        return favoritePlacesDao.getAllFavPlaces()?.map {
            favPlaceDomainModelMapper.toDomainModel(it)
        } ?: listOf()
    }

    override suspend fun getIdAllFavPlaces(): List<Long> {
        return favoritePlacesDao.getIdAllFavPlaces()
    }

    override suspend fun getFavPlaceById(id: Long): FavItemDomainModel {
        return favPlaceDomainModelMapper.toDomainModel(favoritePlacesDao.getFavPlace(id))
    }

    override suspend fun deleteAllFavPlaces() {
        favoritePlacesDao.deleteAllFavPlaces()
    }
}
