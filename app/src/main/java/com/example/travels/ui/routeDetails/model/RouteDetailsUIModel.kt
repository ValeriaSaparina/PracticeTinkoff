package com.example.travels.ui.routeDetails.model

import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.ui.routes.model.RouteUIModel

data class RouteDetailsUIModel(
    val places: List<PlaceUiModel>,
    val route: RouteUIModel
) : DisplayableItem