package com.example.travels.data.places.mapper

import com.example.travels.data.places.remote.response.ItemResponse
import com.example.travels.data.places.remote.response.MetaResponse
import com.example.travels.data.places.remote.response.PlacesResponseModel
import com.example.travels.data.places.remote.response.ReviewResponse
import com.example.travels.domain.places.model.ErrorDomainModel
import com.example.travels.domain.places.model.ItemDomainModel
import com.example.travels.domain.places.model.MetaDomainModel
import com.example.travels.domain.places.model.PlacesDomainModel
import com.example.travels.domain.places.model.ResultDomainModel
import com.example.travels.domain.places.model.ReviewDomainModel
import com.example.travels.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesDomainModelMapper @Inject constructor() {

    fun mapResponseToDomainModel(response: PlacesResponseModel?): PlacesDomainModel {
        return response?.let {
            with(it) {
                val meta = mapMetaResponseToMetaDomainModel(meta)

                val result = ResultDomainModel(
                    items = result?.items?.map { item ->
                        mapItemResponseToItemDomainModel(item)
                    }
                        ?: listOf(),
                    total = result?.total ?: 0
                )


                PlacesDomainModel(
                    meta = meta,
                    result = result
                )
            }
        } ?: throw NullPointerException("response is null")
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

    private fun mapItemResponseToItemDomainModel(item: ItemResponse?): ItemDomainModel? {
        return item?.let {
            ItemDomainModel(
                id = it.id,
                type = it.type,
                name = it.name,
                addressComment = it.addressComment ?: "",
                addressName = it.addressName ?: "",
                review = mapReviewResponseToReviewDomainModel(it.review)
            )
        }
    }

    private fun mapReviewResponseToReviewDomainModel(review: ReviewResponse?): ReviewDomainModel {
        return ReviewDomainModel(
            rating = review?.rating ?: Constants.EMPTY_RATING
        )

    }

}