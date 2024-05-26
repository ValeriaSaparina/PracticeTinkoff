package com.example.travels.data.routes.mapper

import com.example.travels.data.routes.entity.FavoriteRouteEntity
import com.example.travels.data.routes.model.RouteDataModel
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.routes.model.RouteDomainModel
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class RouteDomainMapper @Inject constructor() {
    private val defaultUser = UserModel("", "", "", "")

    fun toDomainModel(route: RouteDataModel?): RouteDomainModel {
        return route?.run {
            RouteDomainModel(
                id = id,
                name = name,
                author = author,
                type = type,
                rating = rating,
                placesId = placesId,
                isFav = isFav,
            )
        } ?: RouteDomainModel(
            id = "",
            name = "",
            author = defaultUser,
            type = "",
            rating = 0f,
            placesId = listOf(),
            isFav = false,
        )
    }

    fun toDataModel(doc: DocumentSnapshot?): RouteDataModel {
        return doc?.run {
            RouteDataModel(
                id = id,
                name = data?.get("name").toString(),
                type = data?.get("type").toString(),
                author = defaultUser.copy(id = data?.get("user_id").toString()),
                rating = data?.get("rating").toString().toFloat(),
                placesId = listOf(),
                isFav = false
            )
        } ?: RouteDataModel(
            id = "",
            name = "",
            rating = 0f,
            author = defaultUser,
            isFav = false,
            placesId = listOf(),
            type = ""
        )
    }

    fun toEntity(route: RouteDomainModel?): FavoriteRouteEntity {
        return route?.run {
            FavoriteRouteEntity(
                id = id,
                name = name,
                authorId = author.id,
                type = type,
                rating = rating,
                noteId = -1
            )
        } ?: FavoriteRouteEntity(
            id = "",
            name = "",
            authorId = "",
            type = "",
            rating = 0f,
            noteId = -1
        )
    }

    fun toDomainModel(entity: FavoriteRouteEntity?): RouteDomainModel {
        return entity?.run {
            RouteDomainModel(
                id = id,
                name = name,
                author = defaultUser.copy(id = entity.authorId),
                type = type,
                rating = rating,
                placesId = listOf(), // TODO: list of places from Room
                isFav = false,
            )
        } ?: RouteDomainModel(
            id = "",
            name = "",
            author = defaultUser,
            type = "",
            rating = 0f,
            placesId = listOf(),
            isFav = false,
        )
    }

    fun toDomainModel(entities: List<FavoriteRouteEntity>?): List<RouteDomainModel> {
        return entities?.map { toDomainModel(it) } ?: listOf()
    }


}
