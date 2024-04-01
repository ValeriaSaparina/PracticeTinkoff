package com.example.travels.data.places.remote.interceptor

import com.example.travels.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url.newBuilder().addQueryParameter(
            name = Constants.APP_ID,
            value = Constants.API_KEY
        ).build()

        val builderRequest = chain.request().newBuilder().url(newUrl)
        return chain.proceed(builderRequest.build())
    }
}