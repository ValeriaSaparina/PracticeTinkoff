package com.example.travels.ui.places.mapper

import com.example.travels.domain.places.model.FavItemDomainModel
import com.example.travels.domain.places.model.MetaDomainModel
import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.domain.places.model.ResultDomainModel
import com.example.travels.domain.places.model.ReviewDomainModel
import com.example.travels.ui.places.model.MetaUiModel
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.ui.places.model.PlacesResponseUiModel
import com.example.travels.ui.places.model.ResultUiModel
import com.example.travels.ui.places.model.ReviewUiModel
import com.example.travels.utils.ApiErrors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesUiModelMapper @Inject constructor() {

    fun mapDomainToUiModel(domainModel: PlacesDomainModel?): PlacesResponseUiModel {
        return if (domainModel != null) {
            with(domainModel) {
                PlacesResponseUiModel(
                    meta = mapMetaDomainToMetaUi(meta),
                    result = mapResultDomainToResultUiModel(result)
                )
            }
        } else {
            PlacesResponseUiModel(
                meta = MetaUiModel(code = -1, error = ApiErrors.UNEXPECTED),
                result = null
            )
        }
    }

    private fun mapResultDomainToResultUiModel(result: ResultDomainModel?): ResultUiModel? {
        return if (result != null) ResultUiModel(
            items = result.items.map {
                mapItemDomainToItemUiModel(it)
            },
            total = result.total
        ) else {
            null
        }
    }

    fun mapItemDomainToItemUiModel(item: PlaceDomainModel?): PlaceUiModel {
        return item?.run {
            val info = name.split(", ")
            return PlaceUiModel(
                id = id,
                type = type,
                name = info.getOrNull(0) ?: "",
                description = info.getOrNull(1) ?: "",
                address = "$addressName; $addressComment",
                review = mapReviewDomainToUiModel(review),
                isFav = isFav
            )
        } ?: PlaceUiModel(
            id = "",
            type = "",
            name = "",
            description = "",
            address = "",
            review = ReviewUiModel(0.0f),
            isFav = false,
        )
    }

    private fun mapReviewDomainToUiModel(review: ReviewDomainModel): ReviewUiModel {
        return ReviewUiModel(
            rating = review.rating
        )
    }

    private fun mapMetaDomainToMetaUi(metaDomain: MetaDomainModel): MetaUiModel {
        return MetaUiModel(
            code = metaDomain.code,
            error = ApiErrors.mapToApiError(metaDomain.code)
        )
    }

    fun toFavItemDomainModel(item: PlaceUiModel): FavItemDomainModel {
        return with(item) {
            FavItemDomainModel(
                id = id.toLong(),
                type = type,
                name = name,
                description = description,
                address = address,
                review = review.rating,
            )
        }
    }

    fun toUiModel(item: FavItemDomainModel): PlaceUiModel {
        return with(item) {
            PlaceUiModel(
                id = id.toString(),
                type = type,
                name = name,
                description = description,
                address = address,
                review = ReviewUiModel(review),
                isFav = true
            )
        }
    }

    fun toUiModel(items: List<FavItemDomainModel>): List<PlaceUiModel> {
        val result = mutableListOf<PlaceUiModel>()
        items.forEach {
            result.add(toUiModel(it))
        }
        return result
    }

}