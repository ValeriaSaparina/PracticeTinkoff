package com.example.travels.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.travels.di.DataContainer
import com.example.travels.domain.auth.SignUpUserUseCase
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUserUseCase: SignUpUserUseCase
) : ViewModel() {


    private val _singingUp = MutableLiveData(false)
    val signingUp: LiveData<Boolean> get() = _singingUp

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> get() = _error

    fun onSignUpClick(email: String, firstname: String, lastname: String, password: String) {
        signUp(email, firstname, lastname, password)
    }

    private fun signUp(email: String, firstname: String, lastname: String, password: String) {
        viewModelScope.launch {
            try {
                _singingUp.value = true
                signUpUserUseCase(email, firstname, lastname, password)
            } catch (e: Throwable) {
                _error.value = e
            } finally {
                _singingUp.value = false
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val useCase = DataContainer.signUpUserUseCase
                SignUpViewModel(useCase)
            }
        }
    }

}