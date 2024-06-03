package com.example.travels.ui.favoritesRoutes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.routes.usercase.AddNewFavRouteUseCase
import com.example.travels.domain.routes.usercase.DeleteFavRouteUseCase
import com.example.travels.domain.routes.usercase.GetFavoriteRoutesUseCase
import com.example.travels.ui.routes.mapper.RoutesUiModelMapper
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteRoutesViewModel @Inject constructor(
    private val getAllFavoriteRoutesUseCase: GetFavoriteRoutesUseCase,
    private val deleteFavRouteUseCase: DeleteFavRouteUseCase,
    private val addNewFavRouteUseCase: AddNewFavRouteUseCase,
    private val mapper: RoutesUiModelMapper
) : ViewModel() {
    private val _process = MutableStateFlow<Boolean?>(null)
    val process: StateFlow<Boolean?> get() = _process

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _placesResult = MutableStateFlow<List<RouteUIModel>?>(null)
    val placesResult: StateFlow<List<RouteUIModel>?> get() = _placesResult

    fun getFavPlaces() {
        viewModelScope.launch {
            _process.emit(true)
            getAllFavoriteRoutesUseCase.invoke()
                .onSuccess {
                    _placesResult.emit(
                        it.map { place ->
                            mapper.mapToUiModel(place)
                        }
                    )
                }
                .onFailure {
                    Log.d("FAVORITE_PLACES", it.message.toString())
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
            _process.emit(false)
        }
    }

    fun onFavIcClicked(item: RouteUIModel) {
        viewModelScope.launch {
            if (item.isFav) {
                deleteFavRouteUseCase.invoke(item)
            } else {
                addNewFavRouteUseCase.invoke(item)
            }
            item.isFav = !item.isFav
        }
    }
}
