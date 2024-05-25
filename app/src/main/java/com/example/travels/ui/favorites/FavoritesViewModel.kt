package com.example.travels.ui.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.places.usecase.DeleteFromFavPlacesUseCase
import com.example.travels.domain.places.usecase.GetFavoritePlacesUseCase
import com.example.travels.domain.routes.usercase.DeleteFavRouteUseCase
import com.example.travels.domain.routes.usercase.GetFavoriteRoutesUseCase
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.ui.routes.mapper.RoutesUiModelMapper
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritePlacesUseCase: GetFavoritePlacesUseCase,
    private val deleteFromFavPlacesUseCase: DeleteFromFavPlacesUseCase,
    private val placesMapper: PlacesUiModelMapper,
    private val getFavoriteRoutesUseCase: GetFavoriteRoutesUseCase,
    private val deleteFromFavRoutesUseCase: DeleteFavRouteUseCase,
    private val routesMapper: RoutesUiModelMapper,
) : ViewModel() {

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _resultPlaces = MutableStateFlow<List<PlaceUiModel>?>(null)
    val resultPlaces: StateFlow<List<PlaceUiModel>?> get() = _resultPlaces

    private val _resultRoutes = MutableStateFlow<List<RouteUIModel>?>(null)
    val resultRoutes: StateFlow<List<RouteUIModel>?> get() = _resultRoutes

    fun onPlaceFavIcClicked(item: PlaceUiModel) {
        viewModelScope.launch {
            if (item.isFav) {
                deleteFromFavPlacesUseCase.invoke(item.id.toLong())
                item.isFav = !item.isFav
                val newList = _resultPlaces.value?.toMutableList()
                newList?.remove(item)
                _resultPlaces.emit(newList)
            }
        }
    }

    fun getFavoritePlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoritePlacesUseCase.invoke(5)
                .onSuccess {
                    _resultPlaces.emit(placesMapper.toUiModel(it))
                }
                .onFailure {
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
        }
    }


    fun onRouteFavIcClicked(item: RouteUIModel) {
        viewModelScope.launch {
            if (item.isFav) {
                deleteFromFavRoutesUseCase.invoke(item)
                item.isFav = !item.isFav
                val newList = _resultRoutes.value?.toMutableList()
                newList?.remove(item)
                _resultRoutes.emit(newList)
            }
        }
    }

    fun getFavoriteRoutes() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteRoutesUseCase.invoke(5)
                .onSuccess {
                    _resultRoutes.emit(routesMapper.mapTUiModel(it))
                }
                .onFailure {
                    Log.d("ROUTES", it.toString())
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
        }
    }

}