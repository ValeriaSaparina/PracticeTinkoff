package com.example.travels.data.routes.repository

import com.example.travels.data.routes.dao.FavoriteRoutesDao
import com.example.travels.data.routes.mapper.RouteDomainMapper
import com.example.travels.data.routes.model.RouteDataModel
import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoutesRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val favoriteRoutesDao: FavoriteRoutesDao,
    private val mapper: RouteDomainMapper,
) : RoutesRepository {
    override suspend fun searchRoutes(query: String): List<RouteDataModel> {
        return db.collection(ROUTES_COLLECTION_PATH).whereGreaterThanOrEqualTo("name", query).get()
            .await()
            .map {
                mapper.toDataModel(it)
            }
    }


    override suspend fun getRoute(id: String): RouteDataModel {
        return mapper.toDataModel(
            db.collection(ROUTES_COLLECTION_PATH).document("test").get().await()
        )
    }

    override suspend fun addNewFavRoute(route: RouteDomainModel) {
        favoriteRoutesDao.addNewRoute(mapper.toEntity(route))
    }

    override suspend fun deleteFavRoute(id: String) {
        favoriteRoutesDao.deleteFavRoute(id)
    }

    override suspend fun getAllFavRoutes(): List<RouteDomainModel> {
        return favoriteRoutesDao.getAllFavRoutes()?.map { entity ->
            mapper.toDomainModel(entity)
        } ?: listOf()
    }

    override suspend fun getFavRouteById(id: String): RouteDomainModel {
        return mapper.toDomainModel(favoriteRoutesDao.getFavRoute(id))
    }

    override suspend fun deleteAllFavRoutes() {
        favoriteRoutesDao.deleteAllFavRoutes()
    }

    override suspend fun getIdAllFavRoutes(): List<String> {
        return favoriteRoutesDao.getIdAllFavRoutes()
    }


    companion object {
        private const val ROUTES_COLLECTION_PATH = "routes"
    }

}
