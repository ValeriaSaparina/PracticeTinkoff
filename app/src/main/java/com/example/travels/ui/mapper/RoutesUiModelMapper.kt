package com.example.travels.ui.mapper

import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.ui.routes.model.RouteUIModel
import javax.inject.Inject

class RoutesUiModelMapper @Inject constructor() {
    fun mapToUiModel(route: RouteDomainModel): RouteUIModel {
        return with(route) {
            RouteUIModel(
                id = id,
                name = name,
                author = author,
                type = type,
                rating = rating,
                isFav = isFav
            )
        }
    }

    fun toDomainModel(route: RouteUIModel): RouteDomainModel {
        return with(route) {
            RouteDomainModel(
                id = id,
                name = name,
                author = author,
                type = type,
                rating = rating,
                isFav = isFav
            )
        }
    }

    fun mapTUiModel(routes: List<RouteDomainModel>): List<RouteUIModel> {
        return routes.map { mapToUiModel(it) }
    }

}
