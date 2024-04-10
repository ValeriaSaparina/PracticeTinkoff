package com.example.travels.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.places.usercase.GetPlacesByQueryUseCase
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlacesUiModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val getPlacesByQueryUseCase: GetPlacesByQueryUseCase,
    private val mapper: PlacesUiModelMapper,
) : ViewModel() {

    private var loadingPlacesJob: Job? = null

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _result = MutableStateFlow<PlacesUiModel?>(null)
    val result: StateFlow<PlacesUiModel?> get() = _result

    fun onLoadPlacesClick(query: String = "") {
        if (loadingPlacesJob?.isActive != false) {
            loadingPlacesJob?.cancel()
        }
        loadPlaces(query)
    }

    private fun loadPlaces(query: String) {
        loadingPlacesJob = viewModelScope.launch {
            val result = getPlacesByQueryUseCase.invoke(query)
            result.onFailure {
                _error.value = if (it is NullPointerException) {
                    NetworkErrors.EMPTY_RESPONSE
                } else {
                    NetworkErrors.UNEXPECTED
                }
            }.onSuccess {
                _result.value = mapper.mapDomainToUiModel(it)
            }
        }
    }

}