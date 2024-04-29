package com.example.travels.data.places.remote.mapper

import com.example.travels.data.places.remote.response.ItemResponse
import com.example.travels.data.places.remote.response.MetaResponse
import com.example.travels.data.places.remote.response.PlacesResponseModel
import com.example.travels.data.places.remote.response.ReviewResponse
import com.example.travels.domain.places.model.ErrorDomainModel
import com.example.travels.domain.places.model.MetaDomainModel
import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.domain.places.model.ResultDomainModel
import com.example.travels.domain.places.model.ReviewDomainModel
import com.example.travels.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesResponseDomainModelMapper @Inject constructor() {

    fun mapResponseToDomainModel(response: PlacesResponseModel?): PlacesDomainModel {
        return response?.let {
            with(it) {
                val meta = mapMetaResponseToMetaDomainModel(meta)
                val result = ResultDomainModel(
                    items = mapItemResponseToItemDomainModel(result?.items),
                    total = result?.total ?: 0
                )

                PlacesDomainModel(
                    meta = meta,
                    result = result
                )
            }
        } ?: throw NullPointerException("response is null")
    }

    fun mapItemResponseToItemDomainModel(items: List<ItemResponse>?): List<PlaceDomainModel> {
        val result = mutableListOf<PlaceDomainModel>()
        items?.forEach {
            if (!it.type.contains("adm_")) {
                result.add(mapItemResponseToItemDomainModel(it))
            }
        }
        return result
    }

    private fun mapMetaResponseToMetaDomainModel(meta: MetaResponse): MetaDomainModel {
        return MetaDomainModel(
            code = meta.code,
            error = ErrorDomainModel(
                type = meta.error?.type ?: "",
                message = meta.error?.message ?: ""
            )
        )
    }

    fun mapItemResponseToItemDomainModel(item: ItemResponse?): PlaceDomainModel {
        return item?.let {
            PlaceDomainModel(
                id = it.id,
                type = it.type,
                name = it.name,
                addressComment = it.addressComment ?: "",
                addressName = it.addressName ?: "",
                review = mapReviewResponseToReviewDomainModel(it.review),
                isFav = false
            )
        } ?: PlaceDomainModel(
            id = "",
            type = "",
            name = "",
            addressComment = "",
            addressName = "",
            review = ReviewDomainModel(0.0f),
            isFav = false
        )
    }

    private fun mapReviewResponseToReviewDomainModel(review: ReviewResponse?): ReviewDomainModel {
        return ReviewDomainModel(
            rating = review?.rating ?: Constants.EMPTY_RATING
        )

    }

}