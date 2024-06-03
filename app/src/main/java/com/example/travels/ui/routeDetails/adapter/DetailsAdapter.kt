package com.example.travels.ui.routeDetails.adapter

import com.example.travels.domain.auth.model.UserModel
import com.example.travels.ui.base.DisplayableItem
import com.example.travels.ui.routes.model.RouteUIModel
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class DetailsAdapter(
    sendReview: (String, String) -> Unit,
    onFavIcClicked: (RouteUIModel) -> Unit,
    onAuthorClicked: (UserModel) -> Unit
) :
    ListDelegationAdapter<List<DisplayableItem>>(
        reviewsAdapterDelegate(onAuthorClicked),
        routeDetailsAdapterDelegate(sendReview, onFavIcClicked, onAuthorClicked)
    )