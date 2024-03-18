package com.example.travels.ui.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.usecase.auth.SignUpUserUseCase
import com.example.travels.domain.usecase.validation.EmailValidationUseCase
import com.example.travels.domain.usecase.validation.NameValidationUseCase
import com.example.travels.domain.usecase.validation.PasswordValidationUseCase
import com.example.travels.domain.usecase.validation.SamePasswordUseCase
import com.example.travels.utils.AuthErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUserUseCase: SignUpUserUseCase,
    private val emailValidationUseCase: EmailValidationUseCase,
    private val nameValidationUseCase: NameValidationUseCase,
    private val passwordValidationUseCase: PasswordValidationUseCase,
    private val samePasswordUseCase: SamePasswordUseCase,
) : ViewModel() {

    private val _signingUp = MutableStateFlow(false)

    private val _error = MutableStateFlow<AuthErrors?>(null)
    val error: StateFlow<AuthErrors?> get() = _error

    fun onSignUpClick(
        email: String,
        firstname: String,
        lastname: String,
        password: String,
        confirmPassword: String
    ) {
        if (isValidData(email, firstname, lastname, password, confirmPassword)) {
            if (!_signingUp.value) {
                signUp(email, firstname, lastname, password)
            } else {
                _error.value = AuthErrors.WAIT
            }
        } else {
            _error.value = AuthErrors.INVALID_DATA
        }
    }

    private fun signUp(
        email: String,
        firstname: String,
        lastname: String,
        password: String,
    ) {
        viewModelScope.launch {
            _signingUp.value = true
            val result = signUpUserUseCase.invoke(email, firstname, lastname, password)
            if (result.isFailure) {
                _error.value = AuthErrors.UNEXPECTED
            }
            _signingUp.value = false
        }
    }

    fun isValidEmail(email: String): Boolean {
        return emailValidationUseCase.invoke(email)
    }

    fun isValidPassword(password: String): Boolean {
        return passwordValidationUseCase.invoke(password)
    }

    fun isSamePassword(password: String, confirmPassword: String): Boolean {
        return samePasswordUseCase.invoke(password, confirmPassword)
    }

    fun isValidName(name: String): Boolean {
        return nameValidationUseCase.invoke(name)
    }

    private fun isValidData(
        email: String,
        firstname: String,
        lastname: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return isValidEmail(email) && isValidPassword(password) && isSamePassword(
            password,
            confirmPassword
        ) && isValidName(firstname) && isValidName(lastname)
    }

}