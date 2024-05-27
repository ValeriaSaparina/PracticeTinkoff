package com.example.travels.domain.routes.repository

import com.example.travels.data.review.ReviewModel
import com.example.travels.data.routes.model.RouteDataModel
import com.example.travels.domain.review.model.UserReviewDomainModel
import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.ui.places.model.PlaceUiModel

interface RoutesRepository {
    suspend fun searchRoutes(query: String): List<RouteDataModel>

    suspend fun getRoute(id: String): RouteDataModel?
    suspend fun addNewFavRoute(route: RouteDomainModel)
    suspend fun deleteFavRoute(id: String)
    suspend fun getAllFavRoutes(): List<RouteDomainModel>
    suspend fun getFavRouteById(id: String): RouteDomainModel
    suspend fun deleteAllFavRoutes()
    suspend fun getIdAllFavRoutes(): List<String>
    suspend fun getFavRoutes(n: Int): List<RouteDomainModel>
    suspend fun getAllReviewsByRoute(routeId: String): List<UserReviewDomainModel>
    suspend fun addReview(review: ReviewModel): UserReviewDomainModel
    suspend fun createRoute(name: String, type: String, places: List<PlaceUiModel>)
}
