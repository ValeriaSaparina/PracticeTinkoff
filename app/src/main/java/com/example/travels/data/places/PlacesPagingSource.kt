package com.example.travels.data.places

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.travels.data.places.remote.PlacesApi
import com.example.travels.data.places.remote.mapper.PlacesResponseDomainModelMapper
import com.example.travels.domain.places.model.PlaceDomainModel
import com.example.travels.domain.places.repository.PlacesRepository
import retrofit2.HttpException
import java.io.IOException
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
        return try {
            val page = params.key ?: 1
            val data = placesApi.getPlacesByQueryPage(
                query = query,
                page = page
            )
            val favorites = repository.getIdAllFavPlaces()
            val places = mutableListOf<PlaceDomainModel>()

            mapperDomainModel.mapResponseToDomainModel(data)
                .result?.items?.onEach {
                    places.add(it.copy(isFav = favorites.contains(it.id.toLong())))
                }

            LoadResult.Page(
                data = places,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data?.meta?.code != 200) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}