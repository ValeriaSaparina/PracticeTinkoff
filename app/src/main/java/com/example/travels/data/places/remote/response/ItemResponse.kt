package com.example.travels.data.places.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemResponse(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("name") val name: String,
    @SerialName("address_name") val addressName: String? = null,
    @SerialName("address_comment") val addressComment: String? = null,
    @SerialName("reviews") val review: ReviewResponse? = null,
)

@Serializable
data class ReviewResponse(
    @SerialName("rating") val rating: Float,
)
