package com.example.travels.data.places.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.travels.data.places.PlacesPagingSource
import com.example.travels.data.places.local.dao.FavoritePlacesDao
import com.example.travels.data.places.local.entity.FavoritePlacesEntity
import com.example.travels.data.places.local.mapper.FavPlaceDomainModelMapper
import com.example.travels.data.places.remote.PlacesApi
import com.example.travels.data.places.remote.mapper.PlacesResponseDomainModelMapper
import com.example.travels.data.review.ReviewModel
import com.example.travels.data.review.mapper.ReviewDomainModelMapper
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.auth.repositoty.UserRepository
import com.example.travels.domain.places.model.FavItemDomainModel
import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.domain.places.model.ReviewDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.domain.review.model.UserReviewDomainModel
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
    private val db: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val favPlaceDomainModelMapper: FavPlaceDomainModelMapper,
    private val responseDomainModelMapper: PlacesResponseDomainModelMapper,
    private val reviewDomainModelMapper: ReviewDomainModelMapper,
) : PlacesRepository {

    private val reviewsDoc = db.collection(PLACES_REVIEWS)
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

    override suspend fun getPlaceById(id: Long): PlaceDomainModel {
        return responseDomainModelMapper.mapItemResponseToItemDomainModel(
            placesApi.getPlaceById(id)?.result?.items?.get(0)
        ).copy(isFav = isFavPlace(id), review = ReviewDomainModel(getPlaceRating(id)))
    }

    private suspend fun isFavPlace(placeId: Long): Boolean {
        return favoritePlacesDao.getFavPlace(placeId) != null
    }

    private suspend fun getPlaceRating(placeId: Long): Float {
        val reviews = getAllReviewsByPlace(placeId)
        val averageRating = reviews.sumOf { it.rating } / reviews.size
        return if (averageRating.isNaN()) 0.0f else averageRating.toFloat()
    }

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

    override suspend fun addReview(review: ReviewModel): UserReviewDomainModel {
        val data = with(review) {
            hashMapOf(
                USER_ID to userId,
                PLACE_ID to placeId,
                TEXT to text,
                RATING to rating
            )
        }
        val currentUser = userRepository.getCurrentUserFromRemote()
        var result: UserReviewDomainModel? = null
        reviewsDoc.add(data).await().get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result = reviewDomainModelMapper.toDomainModel(
                        task.result!!,
                        currentUser
                    )
                }
            }
            .addOnFailureListener {}
            .await()
        return result!!
    }

    override suspend fun getAllReviewsByPlace(placeId: Long): List<UserReviewDomainModel> {
        val result = mutableListOf<UserReviewDomainModel>()
        reviewsDoc.whereEqualTo(PLACE_ID, placeId.toString()).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.addAll(
                        task.result.documents.map {
                            reviewDomainModelMapper.toDomainModel(
                                it,
                                UserModel("-1", "email", "firstname", "lastname")
                            )

                        }
                    )
                }
            }
            .addOnFailureListener {
                Log.d("REVIEW", it.toString())
            }
            .await()
        return result
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
        const val PLACES_REVIEWS: String = "place_reviews"
        const val USER_ID: String = "user_id"
        const val PLACE_ID: String = "place_id"
        const val TEXT: String = "text"
        const val RATING: String = "rating"
        const val FAVORITE_PLACES_COLLECTION_PATH = "favorite_places"
    }
}
