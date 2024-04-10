package com.example.travels.ui.places.mapper

import com.example.travels.domain.places.model.ItemDomainModel
import com.example.travels.domain.places.model.MetaDomainModel
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.domain.places.model.ResultDomainModel
import com.example.travels.domain.places.model.ReviewDomainModel
import com.example.travels.ui.places.model.ItemUiModel
import com.example.travels.ui.places.model.MetaUiModel
import com.example.travels.ui.places.model.PlacesUiModel
import com.example.travels.ui.places.model.ResultUiModel
import com.example.travels.ui.places.model.ReviewUiModel
import com.example.travels.utils.ApiErrors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesUiModelMapper @Inject constructor() {

    fun mapDomainToUiModel(domainModel: PlacesDomainModel?): PlacesUiModel {
        return if (domainModel != null) {
            with(domainModel) {
                PlacesUiModel(
                    meta = mapMetaDomainToMetaUi(meta),
                    result = mapResultDomainToResultUiModel(result)
                )
            }
        } else {
            PlacesUiModel(
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

    private fun mapItemDomainToItemUiModel(item: ItemDomainModel?): ItemUiModel? {
        return item?.let {
            val info = it.name.split(", ")
            ItemUiModel(
                id = it.id,
                type = it.type,
                name = info.getOrNull(0) ?: "",
                description = info.getOrNull(1) ?: "",
                address = "${it.addressName}; ${it.addressComment}",
                review = mapReviewDomainToUiModel(it.review)
            )
        }
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

}