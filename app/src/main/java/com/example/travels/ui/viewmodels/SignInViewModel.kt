package com.example.travels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.travels.di.DataContainer
import com.example.travels.domain.auth.SignInUserUseCase
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUserUserCase: SignInUserUseCase
) : ViewModel() {

    private val _signingIn = MutableLiveData(false)
    val signingIn: LiveData<Boolean> get() = _signingIn

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> get() = _error

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