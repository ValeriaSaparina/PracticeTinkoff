package com.example.travels.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.profile.GetCurrentUserUseCase
import com.example.travels.domain.profile.SignOutUseCase
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> get() = _user

    private val _signOut = MutableStateFlow<Boolean?>(null)
    val signOut: StateFlow<Boolean?> get() = _signOut

    fun loadCurrentUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUseCase.invoke()
                .onSuccess {
                    _user.emit(it)
                }
                .onFailure {
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            signOutUseCase.invoke()
                .onSuccess {
                    _signOut.emit(true)
                }
        }
    }

}