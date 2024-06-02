package com.example.travels.ui.createRoute

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.places.usecase.GetFavoritePlacesUseCase
import com.example.travels.domain.routes.usercase.CreateRouteUseCase
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.utils.CreateRouteError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRouteViewModel @Inject constructor(
    private val createRouteUseCase: CreateRouteUseCase,
    private val getFavoritePlacesUseCase: GetFavoritePlacesUseCase,
    private val placesMapper: PlacesUiModelMapper,
) : ViewModel() {

    private val _process = MutableStateFlow<Boolean?>(null)
    val process: StateFlow<Boolean?> get() = _process

    private var _error = MutableStateFlow<CreateRouteError?>(null)
    val error: StateFlow<CreateRouteError?> get() = _error

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

    fun getFavPlaces() {
        viewModelScope.launch {
            _process.emit(true)
            getFavoritePlacesUseCase.invoke()
                .onSuccess {
                    _favPlaces.emit(placesMapper.toUiModel(it))
                }
                .onFailure {
                    Log.d("GET_FAVS", it.toString())
                }
            _process.emit(false)
        }
    }

    fun createRoute(
        name: String,
        type: String
    ) {
        if (isValidData(name, type)) {
            viewModelScope.launch {
                createRouteUseCase.invoke(name, type, places)
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



