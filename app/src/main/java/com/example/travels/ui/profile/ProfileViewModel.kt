package com.example.travels.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.profile.GetUserByIdUseCase
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : ViewModel() {

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> get() = _user

    fun loadUserProfile(id: String) {
        // ?
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.invoke(id)
                .onSuccess {
                    _user.emit(it)
                }
                .onFailure {
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
        }
    }

}