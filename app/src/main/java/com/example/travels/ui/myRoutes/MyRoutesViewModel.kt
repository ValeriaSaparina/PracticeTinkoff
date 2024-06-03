package com.example.travels.ui.myRoutes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.routes.usercase.AddNewFavRouteUseCase
import com.example.travels.domain.routes.usercase.DeleteFavRouteUseCase
import com.example.travels.domain.routes.usercase.GetUserRoutesUseCase
import com.example.travels.ui.routes.mapper.RoutesUiModelMapper
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRoutesViewModel @Inject constructor(
    private val getUserRoutesUseCase: GetUserRoutesUseCase,
    private val deleteFavRouteUseCase: DeleteFavRouteUseCase,
    private val addNewFavRouteUseCase: AddNewFavRouteUseCase,
    private val mapper: RoutesUiModelMapper
) : ViewModel() {
    private val _process = MutableStateFlow<Boolean?>(null)
    val process: StateFlow<Boolean?> get() = _process

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _routesResult = MutableStateFlow<List<RouteUIModel>?>(null)
    val routesResult: StateFlow<List<RouteUIModel>?> get() = _routesResult

    fun getFavPlaces() {
        viewModelScope.launch {
            _process.emit(true)
            getUserRoutesUseCase.invoke()
                .onSuccess {
                    _routesResult.emit(
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
