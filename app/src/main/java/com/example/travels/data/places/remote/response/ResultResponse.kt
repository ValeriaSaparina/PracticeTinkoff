package com.example.travels.data.places.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultResponse(
    @SerialName("items") val items: List<ItemResponse>,
    @SerialName("total") val total: Int
)
