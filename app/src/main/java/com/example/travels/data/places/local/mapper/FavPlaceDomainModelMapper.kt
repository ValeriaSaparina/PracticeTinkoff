package com.example.travels.data.places.local.mapper

import com.example.travels.data.places.local.entity.FavoritePlacesEntity
import com.example.travels.domain.places.model.FavItemDomainModel
import javax.inject.Inject

class FavPlaceDomainModelMapper @Inject constructor() {

    fun toDomainModel(item: FavoritePlacesEntity?): FavItemDomainModel = item?.let {
        with(item) {
            FavItemDomainModel(
                id = id,
                type = type,
                name = name,
                description = description,
                address = address,
                review = review
            )
        }
    } ?: FavItemDomainModel()

    fun toEntity(item: FavItemDomainModel): FavoritePlacesEntity =
        with(item) {
            FavoritePlacesEntity(
                id = id,
                type = type,
                name = name,
                description = description,
                address = address,
                review = review
            )
        }


}