package com.example.travels.domain.routes.usercase

import android.util.Log
import com.example.travels.domain.places.usecase.GetPlaceByIdUseCase
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.ui.routeDetails.model.RouteDetailsUIModel
import com.example.travels.ui.routes.mapper.RoutesUiModelMapper
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetRouteDetailsUseCase @Inject constructor(
    private val getRouteByIdUseCase: GetRouteByIdUseCase,
    private val getPlacesByIdUseCase: GetPlaceByIdUseCase,
    private val placesUiModelMapper: PlacesUiModelMapper,
    private val routesUiModelMapper: RoutesUiModelMapper
) {

    suspend operator fun invoke(id: String): Result<RouteDetailsUIModel> {
        return runSuspendCatching {
            val places = mutableListOf<PlaceUiModel>()
            var route: RouteUIModel? = null
            getRouteByIdUseCase.invoke(id).onSuccess { routeDomain ->
                route = routesUiModelMapper.mapToUiModel(routeDomain)
                Log.d("DETAILS", "$route")
                routeDomain.placesId.forEach { placeId ->
                    getPlacesByIdUseCase.invoke(placeId.toLong())
                        .onSuccess { place ->
                            places.add(placesUiModelMapper.mapItemDomainToItemUiModel(place))
                        }

                }
            }
            if (places.isNotEmpty() && route != null) RouteDetailsUIModel(
                places,
                route!!
            ) else throw IllegalStateException("route is null")
        }
    }

}