package com.example.travels.data.places.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacesResponseModel(
    @SerialName("meta") val meta: MetaResponse,
    @SerialName("result") val result: ResultResponse? = null,
)
