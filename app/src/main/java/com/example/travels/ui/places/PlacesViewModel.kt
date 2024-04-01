package com.example.travels.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.places.usercase.GetPalacesByQueryUseCase
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlacesUiModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val getPalacesByQueryUseCase: GetPalacesByQueryUseCase,
    private val mapper: PlacesUiModelMapper,
) : ViewModel() {

    private val _loadingPlaces = MutableStateFlow(false)
    val loadingPlaces: StateFlow<Boolean> get() = _loadingPlaces

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _result = MutableStateFlow<PlacesUiModel?>(null)
    val result: StateFlow<PlacesUiModel?> get() = _result

    fun onLoadPlacesClick(query: String = "") {
        if (!_loadingPlaces.value) {
            loadPlaces(query)
        } else {
            _error.value = NetworkErrors.WAIT
        }
    }

    private fun loadPlaces(query: String) {
        viewModelScope.launch {
            _loadingPlaces.value = true
            val result = getPalacesByQueryUseCase.invoke(query)
            result.onFailure {
                _error.value = if (it is NullPointerException) {
                    NetworkErrors.EMPTY_RESPONSE
                } else {
                    NetworkErrors.UNEXPECTED
                }
            }.onSuccess {
                _result.value = mapper.mapDomainToUiModel(it)
            }
            _loadingPlaces.value = false
        }
    }

}