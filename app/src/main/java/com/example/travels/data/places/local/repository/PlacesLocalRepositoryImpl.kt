package com.example.travels.data.places.local.repository

import com.example.travels.data.places.local.dao.FavoritePlacesDao
import com.example.travels.data.places.local.entity.FavoritePlacesEntity
import com.example.travels.data.places.local.mapper.FavPlaceDomainModelMapper
import com.example.travels.domain.places.model.FavItemDomainModel
import com.example.travels.domain.places.repository.PlacesLocalRepository
import javax.inject.Inject

class PlacesLocalRepositoryImpl @Inject constructor(
    private val favoritePlacesDao: FavoritePlacesDao,
    private val mapper: FavPlaceDomainModelMapper,
) : PlacesLocalRepository {
    override suspend fun addNewFavPlaces(vararg items: FavItemDomainModel) {
        val entities = mutableListOf<FavoritePlacesEntity>()
        items.forEach {
            entities.add(mapper.toEntity(it))
        }
        favoritePlacesDao.addNewPlaces(*entities.toTypedArray())
    }

    override suspend fun deleteFromFavPlaces(item: FavItemDomainModel) {
        favoritePlacesDao.deleteFavPlace(mapper.toEntity(item))
    }

    override suspend fun getAllFavPlaces(): List<FavItemDomainModel> {
        return favoritePlacesDao.getAllFavPlaces()?.map {
            mapper.toDomainModel(it)
        } ?: listOf()
    }

    override suspend fun getFavPlaceById(id: Long): FavItemDomainModel {
        return mapper.toDomainModel(favoritePlacesDao.getFavPlace(id))
    }

    override suspend fun deleteAllFavPlaces() {
        favoritePlacesDao.deleteAllFavPlaces()
    }
}