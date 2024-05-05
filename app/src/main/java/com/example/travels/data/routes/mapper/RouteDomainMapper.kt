package com.example.travels.data.routes.mapper

import com.example.travels.data.routes.entity.FavoriteRouteEntity
import com.example.travels.data.routes.model.RouteDataModel
import com.example.travels.domain.routes.model.RouteDomainModel
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class RouteDomainMapper @Inject constructor() {
    fun toDomainModel(route: RouteDataModel?): RouteDomainModel {
        return route?.let { r ->
            with(r) {
                RouteDomainModel(
                    id = id,
                    name = name,
                    isFav = false,
                    authorId = "",
                    type = "",
                )
            }
        } ?: RouteDomainModel(
            id = "",
            name = "",
            isFav = false,
            authorId = "",
            type = "",
        )
    }

    fun toDataModel(doc: DocumentSnapshot?): RouteDataModel {
        return doc?.let {
            RouteDataModel(
                id = it.id,
                name = it.data?.get("name").toString()
            )
        } ?: RouteDataModel(
            id = "",
            name = ""
        )
    }

    fun toEntity(route: RouteDomainModel?): FavoriteRouteEntity {
        return route?.let {
            with(route) {
                FavoriteRouteEntity(
                    id = id,
                    name = name,
                    authorId = authorId,
                    type = type,
                    noteId = -1
                )
            }
        } ?: FavoriteRouteEntity(
            id = "",
            name = "",
            authorId = "",
            type = "",
            noteId = -1
        )
    }

    fun toDomainModel(entity: FavoriteRouteEntity?): RouteDomainModel {
        return entity?.let { e ->
            with(e) {
                RouteDomainModel(
                    id = id,
                    name = name,
                    isFav = false,
                    authorId = "",
                    type = "",
                )
            }
        } ?: RouteDomainModel(
            id = "",
            name = "",
            isFav = false,
            authorId = "",
            type = "",
        )
    }


}
