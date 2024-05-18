package com.example.travels.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.auth.usecase.GetAuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
) : ViewModel() {

    private val _authorizationStatus = MutableStateFlow<Boolean?>(null)
    val authorizationStatus: StateFlow<Boolean?> get() = _authorizationStatus

    fun getAuthStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            getAuthStatusUseCase.invoke()
                .onSuccess {
                    _authorizationStatus.emit(it)
                }
        }
    }

}