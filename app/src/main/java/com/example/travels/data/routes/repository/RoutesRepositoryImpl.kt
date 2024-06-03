package com.example.travels.data.routes.repository

import android.util.Log
import com.example.travels.data.review.ReviewModel
import com.example.travels.data.review.mapper.UserReviewDomainModelMapper
import com.example.travels.data.routes.dao.FavoriteRoutesDao
import com.example.travels.data.routes.mapper.RouteDomainMapper
import com.example.travels.data.routes.model.RouteDataModel
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.auth.repositoty.UserRepository
import com.example.travels.domain.review.model.UserReviewDomainModel
import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.ui.places.model.PlaceUiModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoutesRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val favoriteRoutesDao: FavoriteRoutesDao,
    private val userRepository: UserRepository,
    private val mapper: RouteDomainMapper,
    private val reviewDomainModelMapper: UserReviewDomainModelMapper,
) : RoutesRepository {

    private val routeDoc = db.collection(ROUTES_COLLECTION_PATH)
    private val routePlacesDoc = db.collection(ROUTES_PLACES_COLLECTION_PATH)
    private val favoriteRoutesDoc = db.collection(FAVORITE_ROUTES_COLLECTION_PATH)
    private val reviewsDoc = db.collection(ROUTE_REVIEWS_COLLECTION_PATH)

    override suspend fun searchRoutes(query: String): List<RouteDataModel> {
        val routes = routeDoc.whereGreaterThanOrEqualTo("name", query).get()
            .await()
            .map {
                mapper.toDataModel(it)
            }

        return routes.map { route ->
            route.copy(
                isFav = isFavRoute(route.id),
                rating = getRouteRating(route.id),
                author = getRouteAuthor(route.author.id)
            )
        }
    }


    override suspend fun getRoute(id: String): RouteDataModel {
        val route = mapper.toDataModel(
            routeDoc.document(id).get().await()
        )
        return route.copy(
            isFav = isFavRoute(id),
            rating = getRouteRating(id),
            author = getRouteAuthor(route.author.id),
            placesId = getRoutePlaces(id)
        )
    }


    private suspend fun isFavRoute(routeId: String): Boolean {
        return favoriteRoutesDao.getFavRoute(routeId) != null
    }

    private suspend fun getRoutePlaces(routeId: String): List<String> {
        return routePlacesDoc.whereEqualTo(ROUTE_ID, routeId).get().await().documents.map {
            it.getString(PLACE_ID)!!
        }
    }

    private suspend fun getRouteRating(routeId: String): Float {
        val reviews = getAllReviewsByRoute(routeId)
        val averageRating = reviews.sumOf { it.rating } / reviews.size
        return if (averageRating.isNaN()) 0.0f else averageRating.toFloat()
    }

    override suspend fun addNewFavRoute(route: RouteDomainModel) {
        favoriteRoutesDao.addNewRoute(mapper.toEntity(route))
        val data = hashMapOf(USER_ID to auth.uid, ROUTE_ID to route.id)
        favoriteRoutesDoc.add(data)
    }

    override suspend fun deleteFavRoute(id: String) {
        favoriteRoutesDao.deleteFavRoute(id)
        val favId =
            favoriteRoutesDoc.whereEqualTo(USER_ID, auth.uid).whereEqualTo(ROUTE_ID, id).get()
                .await().documents[0].id
        favoriteRoutesDoc.document(favId).delete()
    }

    override suspend fun getAllFavRoutes(): List<RouteDomainModel> {
        return favoriteRoutesDao.getAllFavRoutes()?.map { entity ->
            mapper.toDomainModel(entity)
        }?.map {
            it.copy(
                isFav = true,
                rating = getRouteRating(routeId = it.id),
                author = getRouteAuthor(routeId = it.author.id)
            )
        } ?: listOf()
    }

    private suspend fun getRouteAuthor(routeId: String): UserModel {
        return userRepository.getUserById(routeId)
    }

    override suspend fun getFavRouteById(id: String): RouteDomainModel {
        return mapper.toDomainModel(favoriteRoutesDao.getFavRoute(id))
    }

    override suspend fun deleteAllFavRoutes() {
        favoriteRoutesDao.deleteAllFavRoutes()
    }

    override suspend fun getIdAllFavRoutes(): List<String> {
        return favoriteRoutesDao.getIdAllFavRoutes() ?: listOf()
    }

    override suspend fun getFavRoutes(n: Int): List<RouteDomainModel> {
        return mapper.toDomainModel(favoriteRoutesDao.getFavRoutes(n)).map {
            it.copy(
                isFav = true,
                rating = getRouteRating(it.id),
                author = getRouteAuthor(it.author.id)
            )
        }
    }

    override suspend fun getAllReviewsByRoute(routeId: String): List<UserReviewDomainModel> {
        val allReviews = mutableListOf<UserReviewDomainModel>()
        reviewsDoc.whereEqualTo(ROUTE_ID, routeId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    allReviews.addAll(
                        task.result.documents.map {
                            reviewDomainModelMapper.toDomainModel(
                                it,
                                UserModel(it.getString(USER_ID) ?: "", "", "", "")
                            )
                        }
                    )
                }
            }
            .addOnFailureListener {
                Log.d("REVIEW", it.toString())
            }
            .await()
        val result = mutableListOf<UserReviewDomainModel>()
        allReviews.forEach {
            result.add(it.copy(user = userRepository.getUserById(it.user.id)))
        }
        return result
    }

    override suspend fun addReview(review: ReviewModel): UserReviewDomainModel {
        val data = with(review) {
            hashMapOf(
                USER_ID to userId,
                ROUTE_ID to routeId,
                REVIEW_TEXT to text,
                REVIEW_RATING to rating
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

    override suspend fun createRoute(name: String, type: String, places: List<PlaceUiModel>) {
        val routeData =
            hashMapOf(
                ROUTE_NAME to name,
                ROUTE_TYPE to type,
                ROUTE_RATING to 0,
                USER_ID to auth.uid
            )
        routeDoc.add(routeData).await().get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val routeId = task.result.id
                    places.forEach {
                        val placeData =
                            hashMapOf(
                                PLACE_ID to it.id,
                                ROUTE_ID to routeId
                            )
                        routePlacesDoc.add(placeData)
                    }
                }
            }
            .addOnFailureListener {}
    }

    override suspend fun updateRoute(route: RouteDomainModel) {
        val routeDataModel = mapper.toDataModel(route)
        val routeData =
            mapOf(
                ROUTE_NAME to routeDataModel.name,
                ROUTE_TYPE to routeDataModel.type,
                ROUTE_RATING to routeDataModel.rating,
                USER_ID to routeDataModel.author.id
            )
        val routeId = routeDataModel.id
        val prevPlaces = routePlacesDoc.whereEqualTo(ROUTE_ID, routeId).get()
            .await().documents.map { it.getString(PLACE_ID)!! }
        routeDoc.document(routeId).update(routeData).await()
        routeDataModel.placesId.forEach {
            if (!prevPlaces.contains(it)) {
                val placeData =
                    hashMapOf(
                        PLACE_ID to it,
                        ROUTE_ID to routeId
                    )
                routePlacesDoc.add(placeData)
            }
        }
        prevPlaces.forEach {
            if (!routeDataModel.placesId.contains(it)) {
                routePlacesDoc.whereEqualTo(PLACE_ID, it).get().await().forEach { document ->
                    document.reference.delete()
                }
            }
        }
    }

    override suspend fun getUserRoutes(userid: String): List<RouteDataModel> {
        val routes = routeDoc.whereEqualTo(USER_ID, userid).get()
            .await()
            .map {
                mapper.toDataModel(it)
            }

        return routes.map { route ->
            route.copy(
                isFav = isFavRoute(route.id),
                rating = getRouteRating(route.id),
                author = getRouteAuthor(route.author.id)
            )
        }
    }


    companion object {
        private const val ROUTES_COLLECTION_PATH = "routes"
        private const val ROUTES_PLACES_COLLECTION_PATH = "route_places"
        private const val FAVORITE_ROUTES_COLLECTION_PATH = "favorite_routes"
        private const val ROUTE_REVIEWS_COLLECTION_PATH = "route_reviews"

        private const val USER_ID = "user_id"
        private const val PLACE_ID = "place_id"

        private const val ROUTE_ID = "route_id"
        private const val ROUTE_NAME = "name"
        private const val ROUTE_TYPE = "type"
        private const val ROUTE_RATING = "rating"

        private const val REVIEW_TEXT = "text"
        private const val REVIEW_RATING = "rating"
    }

}
