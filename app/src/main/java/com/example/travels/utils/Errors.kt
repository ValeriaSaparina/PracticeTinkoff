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
    MAX_PAGES,
}

enum class ApiErrors {
    PARAM_IS_EMPTY,
    FORBIDDEN,
    ITEM_NOT_FOUND,
    UNEXPECTED,
    BACKEND_EXCEPTION,
    SERVICE_UNAVAILABLE;

    companion object {
        fun mapToApiError(code: Int?): ApiErrors? {
            return when (code) {
                400 -> PARAM_IS_EMPTY
                403 -> FORBIDDEN
                404 -> ITEM_NOT_FOUND
                408 -> BACKEND_EXCEPTION
                505 -> SERVICE_UNAVAILABLE
                200 -> null
                else -> UNEXPECTED
            }
        }
    }
}