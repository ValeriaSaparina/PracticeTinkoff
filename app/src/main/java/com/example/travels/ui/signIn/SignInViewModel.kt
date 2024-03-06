package com.example.travels.ui.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.travels.di.DataContainer
import com.example.travels.domain.auth.SignInUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUserUserCase: SignInUserUseCase
) : ViewModel() {

    private val _signingIn = MutableStateFlow(false)
    val signingIn: StateFlow<Boolean> get() = _signingIn

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?> get() = _error

    fun onSignUpClick(email: String, password: String) {
        signIn(email, password)
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _signingIn.value = true
                signInUserUserCase(email, password)
            } catch (e: Throwable) {
                _error.value = e
            } finally {
                _signingIn.value = false
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val useCase = DataContainer.signInUserUseCase
                SignInViewModel(useCase)
            }
        }
    }

}