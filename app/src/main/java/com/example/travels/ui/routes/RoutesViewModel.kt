package com.example.travels.ui.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.routes.usercase.AddNewFavRouteUseCase
import com.example.travels.domain.routes.usercase.DeleteFavRouteUseCase
import com.example.travels.domain.routes.usercase.SearchRoutesUseCase
import com.example.travels.ui.routes.mapper.RoutesUiModelMapper
import com.example.travels.ui.routes.model.RouteUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val searchRoutesUseCase: SearchRoutesUseCase,
    private val mapper: RoutesUiModelMapper,
    private val addNewFavRouteUseCase: AddNewFavRouteUseCase,
    private val deleteFavRouteUseCase: DeleteFavRouteUseCase,

    ) : ViewModel() {

    private var _result = MutableStateFlow<List<RouteUIModel>?>(null)
    val result: StateFlow<List<RouteUIModel>?> get() = _result

    private var _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?> get() = _error

    private var _process = MutableStateFlow<Boolean?>(null)
    val process: StateFlow<Boolean?> get() = _process

    fun searchRoutes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _process.emit(true)
            searchRoutesUseCase.invoke(query)
                .onSuccess { list ->
                    _result.emit(
                        list.map { route ->
                            mapper.mapToUiModel(route)
                        }
                    )
                }
                .onFailure { error ->
                    _error.emit(error)
                }
            _process.emit(false)
        }
    }

    fun onFavIcClicked(route: RouteUIModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (route.isFav) {
                deleteFavRouteUseCase.invoke(route)
            } else {
                addNewFavRouteUseCase.invoke(route)
            }
            route.isFav = !route.isFav
        }
    }

}