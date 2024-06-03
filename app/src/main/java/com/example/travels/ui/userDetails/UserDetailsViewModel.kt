package com.example.travels.ui.userDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.profile.GetUserByIdUseCase
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
class UserDetailsViewModel @Inject constructor(
    private val getUserRoutesUseCase: GetUserRoutesUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
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

    private val _userResult = MutableStateFlow<UserModel?>(null)
    val userResult: StateFlow<UserModel?> get() = _userResult


    fun getUserRoutes(id: String) {
        viewModelScope.launch {
            getUserRoutesUseCase.invoke(id)
                .onSuccess {
                    _routesResult.emit(
                        it.map { route ->
                            mapper.mapToUiModel(route)
                        }
                    )
                }
                .onFailure {
                    Log.d("PROFILE", "getting routes: $it")
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
            _process.emit(false)
        }
    }

    fun getUserDetails(id: String) {
        viewModelScope.launch {
            _process.emit(true)
            getUserByIdUseCase.invoke(id)
                .onSuccess {
                    _userResult.emit(it)
                }
                .onFailure {
                    Log.d("PROFILE", "getting user: $it")
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
        }
    }

    fun onFavIcClicked(route: RouteUIModel) {
        viewModelScope.launch {
            if (route.isFav) {
                deleteFavRouteUseCase.invoke(route)
            } else {
                addNewFavRouteUseCase.invoke(route)
            }
            route.isFav = !route.isFav
        }
    }
}