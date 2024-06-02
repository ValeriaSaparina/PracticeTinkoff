package com.example.travels.ui.routeDetails.adapter

import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.routes.model.RouteUIModel
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class DetailsAdapter(
    sendReview: (String, String) -> Unit,
    onFavIcClicked: (RouteUIModel) -> Unit
) :
    ListDelegationAdapter<List<DisplayableItem>>(
        reviewsAdapterDelegate(),
        routeDetailsAdapterDelegate(sendReview, onFavIcClicked)
    )