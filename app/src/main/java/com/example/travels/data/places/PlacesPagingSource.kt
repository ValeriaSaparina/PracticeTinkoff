package com.example.travels.data.places

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.travels.data.places.remote.PlacesApi
import com.example.travels.data.places.remote.mapper.PlacesResponseDomainModelMapper
import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.utils.Constants
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class PlacesPagingSource @Inject constructor(
    private val placesApi: PlacesApi,
    private val mapperDomainModel: PlacesResponseDomainModelMapper,
    private val repository: PlacesRepository,
    private val query: String,
) : PagingSource<Int, PlaceDomainModel>() {

    override fun getRefreshKey(state: PagingState<Int, PlaceDomainModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlaceDomainModel> {
        if (query.isEmpty()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }

        val page = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(Constants.MAX_PAGE_SIZE)
        val placesResult = runSuspendCatching {
            placesApi.getPlacesByQueryPage(
                query = query,
                page = page
            )
        }
        return placesResult.fold(
            onSuccess = { places ->
                val favorites = repository.getIdAllFavPlaces()
                val domainPlaces = mapperDomainModel.mapResponseToDomainModel(places)
                    .result?.items?.map {
                        it.copy(isFav = favorites.contains(it.id.toLong()))
                    }.orEmpty()
                val nextKey = if (domainPlaces.size < pageSize) null else page + 1
                val prevKey = if (page == 1) null else page - 1
                LoadResult.Page(domainPlaces, prevKey, nextKey)
            },
            onFailure = { LoadResult.Error(it) }
        )
    }
}
