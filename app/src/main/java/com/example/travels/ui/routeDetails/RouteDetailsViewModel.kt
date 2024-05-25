package com.example.travels.ui.routeDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.review.GetAllRouteReviewsUseCase
import com.example.travels.domain.review.SendRouteReviewReviewUseCase
import com.example.travels.domain.routes.usercase.GetRouteByIdUseCase
import com.example.travels.ui.routeDetails.review.mapper.ReviewUiModelMapper
import com.example.travels.ui.routeDetails.review.model.UserReviewUiModel
import com.example.travels.ui.routes.mapper.RoutesUiModelMapper
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteDetailsViewModel @Inject constructor(
    private val getRouteByIdUseCase: GetRouteByIdUseCase,
    private val sendRouteReviewReviewUseCase: SendRouteReviewReviewUseCase,
    private val getAllRouteReviewsUseCase: GetAllRouteReviewsUseCase,
    private val routeMapper: RoutesUiModelMapper,
    private val reviewMapper: ReviewUiModelMapper,
) : ViewModel() {

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _placeResult = MutableStateFlow<RouteUIModel?>(null)
    val routeResult: StateFlow<RouteUIModel?> get() = _placeResult

    private val _reviewsResults = MutableStateFlow<List<UserReviewUiModel>?>(null)
    val reviewResults: StateFlow<List<UserReviewUiModel>?> get() = _reviewsResults

    private val _review = MutableStateFlow<UserReviewUiModel?>(null)
    val review: StateFlow<UserReviewUiModel?> get() = _review
    fun getPlaceDetails(id: String) {
        viewModelScope.launch {
            getRouteByIdUseCase.invoke(id)
                .onSuccess {
                    _placeResult.emit(routeMapper.mapToUiModel(it))
                }
                .onFailure {
                    Log.d("DETAILS", "$it")
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
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