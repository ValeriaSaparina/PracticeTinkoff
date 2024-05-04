package com.example.travels.data.places.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaResponse(
    @SerialName("code") val code: Int,
    @SerialName("error") val error: ErrorResponse? = null,
)

@Serializable
data class ErrorResponse(
    @SerialName("type") val type: String,
    @SerialName("message") val message: String
)
