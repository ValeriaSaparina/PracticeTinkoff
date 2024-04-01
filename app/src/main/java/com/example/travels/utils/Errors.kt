package com.example.travels.utils

enum class AuthErrors {
    INVALID_DATA,
    UNEXPECTED,
    WAIT,
    INVALID_CREDENTIALS
}

enum class NetworkErrors {
    EMPTY_RESPONSE,
    WAIT,
    UNEXPECTED,
}

enum class ApiErrors {
    PARAM_IS_EMPTY,
    FORBIDDEN,
    ITEM_NOT_FOUND,
    UNEXPECTED,
    BACKEND_EXCEPTION,
    SERVICE_UNAVAILABLE
}