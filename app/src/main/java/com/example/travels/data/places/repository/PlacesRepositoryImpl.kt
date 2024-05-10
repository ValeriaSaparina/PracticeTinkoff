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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesApi: PlacesApi,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val favoritePlacesDao: FavoritePlacesDao,
    private val favPlaceDomainModelMapper: FavPlaceDomainModelMapper,
    private val responseDomainModelMapper: PlacesResponseDomainModelMapper,
) : PlacesRepository {

    private val favoritePlacesDoc = db.collection(FAVORITE_PLACES_COLLECTION_PATH)

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
        items.forEach {
            val data = hashMapOf(USER_ID to auth.uid, PLACE_ID to it.id)
            favoritePlacesDoc.add(data)
        }
    }

    override suspend fun deleteFromFavPlaces(id: Long) {
        favoritePlacesDao.deleteFavPlace(id)
        val favId = favoritePlacesDoc.whereEqualTo(USER_ID, auth.uid).whereEqualTo(
            PLACE_ID, id
        ).get()
            .await().documents[0].id
        favoritePlacesDoc.document(favId).delete()
    }

    override suspend fun getAllFavPlaces(): List<FavItemDomainModel> {
        return favoritePlacesDao.getAllFavPlaces()?.map {
            favPlaceDomainModelMapper.toDomainModel(it)
        } ?: listOf()
    }

    override suspend fun getFavPlaces(n: Int): List<FavItemDomainModel> {
        return favoritePlacesDao.getFavPlaces(n)?.map {
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

    companion object {
        const val FAVORITE_PLACES_COLLECTION_PATH = "favorite_places"
        private const val USER_ID = "user_id"
        private const val PLACE_ID = "place_id"
    }
}
