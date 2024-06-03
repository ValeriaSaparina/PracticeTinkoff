package com.example.travels.ui.editRoute

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.places.usecase.GetFavoritePlacesUseCase
import com.example.travels.domain.places.usecase.GetPlaceByIdUseCase
import com.example.travels.domain.routes.usercase.GetRouteByIdUseCase
import com.example.travels.domain.routes.usercase.UpdateRouteUseCase
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.ui.routes.mapper.RoutesUiModelMapper
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.CreateRouteError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRouteViewModel @Inject constructor(
    private val updateRouteUseCase: UpdateRouteUseCase,
    private val getRouteByIdUseCase: GetRouteByIdUseCase,
    private val getFavoritePlacesUseCase: GetFavoritePlacesUseCase,
    private val getPlaceByIdUseCase: GetPlaceByIdUseCase,
    private val placesMapper: PlacesUiModelMapper,
    private val routeMapper: RoutesUiModelMapper,
) : ViewModel() {

    private val _process = MutableStateFlow<Boolean?>(null)
    val process: StateFlow<Boolean?> get() = _process

    private val _error = MutableStateFlow<CreateRouteError?>(null)
    val error: StateFlow<CreateRouteError?> get() = _error

    private val _route = MutableStateFlow<RouteUIModel?>(null)
    val route: StateFlow<RouteUIModel?> get() = _route

    private val places = mutableListOf<PlaceUiModel>()

    private val _success = MutableStateFlow<Boolean?>(null)
    val success: StateFlow<Boolean?> get() = _success

    private val _favPlaces = MutableStateFlow<List<PlaceUiModel>?>(null)
    val favPlaces: StateFlow<List<PlaceUiModel>?> get() = _favPlaces

    fun onPlaceClicked(item: PlaceUiModel): Boolean {
        return if (places.contains(item)) {
            places.remove(item)
            false
        } else {
            places.add(item)
            true
        }
    }

    fun getRoute(id: String) {
        viewModelScope.launch {
            _process.emit(true)
            getRouteByIdUseCase.invoke(id)
                .onSuccess { route ->
                    route.placesId.forEach { id ->
                        getPlaceByIdUseCase.invoke(id.toLong())
                            .onSuccess { place ->
                                places.add(placesMapper.mapItemDomainToItemUiModel(place))
                                getFavoritePlacesUseCase.invoke()
                                    .onSuccess { favPlaces ->
                                        val placesList = places.toMutableList()
                                        favPlaces.forEach {
                                            placesMapper.toUiModel(it).let { favPlace ->
                                                if (!placesList.contains(favPlace)) {
                                                    placesList.add(favPlace)
                                                }
                                            }
                                        }
                                        _favPlaces.emit(placesList)
                                    }
                                    .onFailure {
                                        Log.d("EDIT_ROUTE", it.toString())
                                        _error.emit(CreateRouteError.UNEXPECTED)
                                    }
                            }
                            .onFailure {
                                Log.d("EDIT_ROUTE", it.toString())
                                _error.emit(CreateRouteError.UNEXPECTED)
                            }
                        _route.emit(routeMapper.mapToUiModel(route))
                    }

                }
                .onFailure {
                    Log.d("EDIT_ROUTE", it.toString())
                    _error.emit(CreateRouteError.UNEXPECTED)
                }
            _process.emit(false)
        }
    }


    fun saveRoute(
        routeUIModel: RouteUIModel
    ) {
        if (isValidData(routeUIModel.name, routeUIModel.type)) {
            viewModelScope.launch {
                updateRouteUseCase.invoke(routeMapper.toDomainModel(routeUIModel.copy(placesId = places.map { it.id })))
                    .onSuccess {
                        _success.emit(true)
                    }
                    .onFailure {
                        Log.d("GET_FAVS", it.toString())
                        _error.emit(CreateRouteError.UNEXPECTED)
                    }
            }
        }
    }

    private fun isValidData(
        name: String,
        description: String,
    ): Boolean {
        if (name.isEmpty()) {
            _error.value = CreateRouteError.EMPTY_VALUE
            return false
        }
        if (description.isEmpty()) {
            _error.value = CreateRouteError.EMPTY_VALUE
            return false
        }
        if (places.isEmpty()) {
            _error.value = CreateRouteError.EMPTY_LIST
            return false
        }
        return true
    }

}
