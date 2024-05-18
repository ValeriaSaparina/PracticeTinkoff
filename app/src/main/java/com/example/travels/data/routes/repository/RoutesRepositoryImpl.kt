package com.example.travels.data.routes.repository

import com.example.travels.data.routes.dao.FavoriteRoutesDao
import com.example.travels.data.routes.mapper.RouteDomainMapper
import com.example.travels.data.routes.model.RouteDataModel
import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoutesRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val favoriteRoutesDao: FavoriteRoutesDao,
    private val mapper: RouteDomainMapper,
) : RoutesRepository {

    private val routeDoc = db.collection(ROUTES_COLLECTION_PATH)
    private val favoriteRoutesDoc = db.collection(FAVORITE_ROUTES_COLLECTION_PATH)

    override suspend fun searchRoutes(query: String): List<RouteDataModel> {
        return routeDoc.whereGreaterThanOrEqualTo("name", query).get()
            .await()
            .map {
                mapper.toDataModel(it)
            }
    }


    override suspend fun getRoute(id: String): RouteDataModel {
        return mapper.toDataModel(
            routeDoc.document(id).get().await()
        )
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
            it.copy(isFav = true)
        } ?: listOf()
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
            it.copy(isFav = true)
        }
    }


    companion object {
        private const val ROUTES_COLLECTION_PATH = "routes"
        private const val FAVORITE_ROUTES_COLLECTION_PATH = "favorite_routes"
        private const val USER_ID = "user_id"
        private const val ROUTE_ID = "route_id"
    }

}
