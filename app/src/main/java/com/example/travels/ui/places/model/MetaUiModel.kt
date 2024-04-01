package com.example.travels.ui.places.model

import com.example.travels.utils.ApiErrors

data class MetaUiModel(
    val code: Int,
    val error: ApiErrors?,
)