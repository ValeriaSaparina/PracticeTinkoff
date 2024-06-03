package com.example.travels.ui.favoritesPlaces

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.places.usecase.AddNewFavPlaceUseCase
import com.example.travels.domain.places.usecase.DeleteFromFavPlacesUseCase
import com.example.travels.domain.places.usecase.GetFavoritePlacesUseCase
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritePlacesViewModel @Inject constructor(
    private val getAllFavoritePlacesFragment: GetFavoritePlacesUseCase,
    private val deleteFromFavPlacesUseCase: DeleteFromFavPlacesUseCase,
    private val addNewFavPlaceUseCase: AddNewFavPlaceUseCase,
    private val mapper: PlacesUiModelMapper
) : ViewModel() {
    private val _process = MutableStateFlow<Boolean?>(null)
    val process: StateFlow<Boolean?> get() = _process

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _placesResult = MutableStateFlow<List<PlaceUiModel>?>(null)
    val placesResult: StateFlow<List<PlaceUiModel>?> get() = _placesResult

    fun getFavPlaces() {
        viewModelScope.launch {
            _process.emit(true)
            getAllFavoritePlacesFragment.invoke()
                .onSuccess {
                    _placesResult.emit(
                        it.map { place ->
                            mapper.toUiModel(place)
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

    fun onFavIcClicked(item: PlaceUiModel) {
        viewModelScope.launch {
            if (item.isFav) {
                deleteFromFavPlacesUseCase.invoke(item.id.toLong())
            } else {
                addNewFavPlaceUseCase.invoke(item)
            }
            item.isFav = !item.isFav
        }
    }
}
