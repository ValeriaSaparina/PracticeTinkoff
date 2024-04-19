package com.example.travels.ui.places

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.travels.domain.places.usecase.SearchPlacesUseCase
import com.example.travels.ui.places.model.ItemUiModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val searchPlacesUseCase: SearchPlacesUseCase,
) : ViewModel() {

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    suspend fun searchRepos(query: String): Flow<PagingData<ItemUiModel>> {
        searchPlacesUseCase.invoke(query)
            .onSuccess {
                return it
            }
            .onFailure {
                _error.value = NetworkErrors.UNEXPECTED
            }
        return flowOf()
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