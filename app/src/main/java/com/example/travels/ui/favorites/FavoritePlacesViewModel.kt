package com.example.travels.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.places.usecase.DeleteFromFavPlacesUseCase
import com.example.travels.domain.places.usecase.GetFavoritePlacesUseCase
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritePlacesViewModel @Inject constructor(
    private val getFavoritePlacesUseCase: GetFavoritePlacesUseCase,
    private val deleteFromFavPlacesUseCase: DeleteFromFavPlacesUseCase,
    private val mapper: PlacesUiModelMapper,
) : ViewModel() {

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _result = MutableStateFlow<List<PlaceUiModel>?>(null)
    val result: StateFlow<List<PlaceUiModel>?> get() = _result

    fun onFavIcClicked(item: PlaceUiModel) {
        viewModelScope.launch {
            if (item.isFav) {
                deleteFromFavPlacesUseCase.invoke(item.id.toLong())
                item.isFav = !item.isFav
            }
        }
    }

    fun getFavoritePlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoritePlacesUseCase.invoke()
                .onSuccess {
                    _result.emit(mapper.toUiModel(it))
                }
                .onFailure {
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
        }
    }

}