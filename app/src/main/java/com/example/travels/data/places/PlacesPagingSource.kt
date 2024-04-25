package com.example.travels.data.places

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.travels.data.places.remote.PlacesApi
import com.example.travels.data.places.remote.mapper.PlacesResponseDomainModelMapper
import com.example.travels.domain.places.repository.PlacesRepository
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.ItemUiModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PlacesPagingSource @Inject constructor(
    private val placesApi: PlacesApi,
    private val mapperUiModel: PlacesUiModelMapper,
    private val mapperDomainModel: PlacesResponseDomainModelMapper,
    private val repository: PlacesRepository,
    private val query: String,
) : PagingSource<Int, ItemUiModel>() {

    override fun getRefreshKey(state: PagingState<Int, ItemUiModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemUiModel> {
        return try {
            val page = params.key ?: 1
            val data = placesApi.getPlacesByQueryPage(
                query = query,
                page = page
            )
            val favorites = repository.getIdAllFavPlaces()
            val places = mapperUiModel.mapDomainToUiModel(
                mapperDomainModel.mapResponseToDomainModel(data)
            ).result?.items?.onEach {
                it.isFav = favorites.contains(it.id.toLong())
            } ?: listOf()

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