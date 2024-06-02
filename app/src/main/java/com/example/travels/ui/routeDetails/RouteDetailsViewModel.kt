package com.example.travels.ui.routeDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.review.GetAllRouteReviewsUseCase
import com.example.travels.domain.review.SendRouteReviewReviewUseCase
import com.example.travels.domain.routes.usercase.GetRouteDetailsUseCase
import com.example.travels.ui.routeDetails.model.RouteDetailsUIModel
import com.example.travels.ui.routeDetails.review.mapper.ReviewUiModelMapper
import com.example.travels.ui.routeDetails.review.model.UserReviewUiModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteDetailsViewModel @Inject constructor(
    private val sendRouteReviewReviewUseCase: SendRouteReviewReviewUseCase,
    private val getRouteDetailsUseCase: GetRouteDetailsUseCase,
    private val getAllRouteReviewsUseCase: GetAllRouteReviewsUseCase,
    private val reviewMapper: ReviewUiModelMapper,
) : ViewModel() {

    private val _process = MutableStateFlow<Boolean?>(null)
    val process: StateFlow<Boolean?> get() = _process

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _routeResult = MutableStateFlow<RouteDetailsUIModel?>(null)
    val routeResult: StateFlow<RouteDetailsUIModel?> get() = _routeResult

    private val _reviewsResults = MutableStateFlow<List<UserReviewUiModel>?>(null)
    val reviewResults: StateFlow<List<UserReviewUiModel>?> get() = _reviewsResults

    private val _review = MutableStateFlow<UserReviewUiModel?>(null)
    val review: StateFlow<UserReviewUiModel?> get() = _review
    fun getPlaceDetails(id: String) {
        viewModelScope.launch {
            _process.emit(true)
            getRouteDetailsUseCase.invoke(id)
                .onSuccess {
                    _routeResult.emit(it)
                }
                .onFailure {
                    Log.d("DETAILS", "$it")
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
            _process.emit(false)
        }
    }

    fun getAllReviews(routeId: String) {
        viewModelScope.launch {
            getAllRouteReviewsUseCase.invoke(routeId)
                .onSuccess {
                    _reviewsResults.emit(reviewMapper.toUiModel(it))
                }
                .onFailure {
                    Log.d("IN VM", it.toString())
                }
        }
    }

    fun sendReview(routeId: String, rating: String, text: String) {
        viewModelScope.launch {
            sendRouteReviewReviewUseCase.invoke(routeId, rating, text)
                .onFailure {
                    Log.d("REVIEW", "$it")
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
                .onSuccess {
                    _review.emit(reviewMapper.toUiModel(it))
                }
        }
    }

}