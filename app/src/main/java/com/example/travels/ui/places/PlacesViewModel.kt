package com.example.travels.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.travels.domain.places.usecase.AddNewFavPlaceUseCase
import com.example.travels.domain.places.usecase.DeleteFromFavPlacesUseCase
import com.example.travels.domain.places.usecase.SearchPlacesUseCase
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val searchPlacesUseCase: SearchPlacesUseCase,
    private val addNewFavPlaceUseCase: AddNewFavPlaceUseCase,
    private val deleteFromFavPlacesUseCase: DeleteFromFavPlacesUseCase,
    private val mapper: PlacesUiModelMapper,
) : ViewModel() {

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _result = MutableStateFlow<Flow<PagingData<PlaceUiModel>>?>(null)
    val result: StateFlow<Flow<PagingData<PlaceUiModel>>?> get() = _result
    fun searchRepos(query: String) {

        viewModelScope.launch(Dispatchers.IO) {
            searchPlacesUseCase.invoke(query)
                .onSuccess {
                    _result.value = it.map { pagingData ->
                        pagingData.map { place ->
                            mapper.mapItemDomainToItemUiModel(place)
                        }
                    }.cachedIn(viewModelScope)
                }
                .onFailure {
                    _error.value = NetworkErrors.UNEXPECTED
                }
        }
    }

    fun onFavIcClicked(item: PlaceUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (item.isFav) {
                deleteFromFavPlacesUseCase.invoke(item.id.toLong())
            } else {
                addNewFavPlaceUseCase.invoke(item)
            }
            item.isFav = !item.isFav
        }
    }

//    val placesList: StateFlow<Flow<PagingData<PlacesUiModel>>?> get() = _placesList
//    private val queryFlow = MutableSharedFlow<String>()
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    val result: Flow<PlacesUiModel?> = queryFlow
//        .transformLatest { query ->
//            getPlacesByQueryUseCase.invoke(query)
//                .onFailure {
//                    _error.value = if (it is NullPointerException) {
//                        NetworkErrors.EMPTY_RESPONSE
//                    } else {
//                        NetworkErrors.UNEXPECTED
//                    }
//                }.onSuccess {
//                    emit(mapper.mapDomainToUiModel(it))
//                }
//        }
//        .flowOn(Dispatchers.IO)


}