package com.example.travels.ui.placeDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travels.domain.places.usecase.GetAllReviewsUseCase
import com.example.travels.domain.places.usecase.GetPlaceByIdUseCase
import com.example.travels.domain.places.usecase.SendPlaceReviewUseCase
import com.example.travels.ui.placeDetails.review.mapper.ReviewUiModelMapper
import com.example.travels.ui.placeDetails.review.model.ReviewUiModel
import com.example.travels.ui.places.mapper.PlacesUiModelMapper
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.utils.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    private val getPlaceByIdUseCase: GetPlaceByIdUseCase,
    private val sendPlaceReviewUseCase: SendPlaceReviewUseCase,
    private val getAllReviewsUseCase: GetAllReviewsUseCase,
    private val placeMapper: PlacesUiModelMapper,
    private val reviewMapper: ReviewUiModelMapper,
) : ViewModel() {

    private val _error = MutableStateFlow<NetworkErrors?>(null)
    val error: StateFlow<NetworkErrors?> get() = _error

    private val _placeResult = MutableStateFlow<PlaceUiModel?>(null)
    val placeResult: StateFlow<PlaceUiModel?> get() = _placeResult

    private val _reviewsResults = MutableStateFlow<List<ReviewUiModel>?>(null)
    val reviewResults: StateFlow<List<ReviewUiModel>?> get() = _reviewsResults

    private val _review = MutableStateFlow<ReviewUiModel?>(null)
    val review: StateFlow<ReviewUiModel?> get() = _review
    fun getPlaceDetails(id: Long) {
        viewModelScope.launch {
            getPlaceByIdUseCase.invoke(id)
                .onSuccess {
                    _placeResult.emit(placeMapper.mapItemDomainToItemUiModel(it))
                }
                .onFailure {
                    _error.emit(NetworkErrors.UNEXPECTED)
                }
        }
    }

    fun getAllReviews(placeId: Long) {
        viewModelScope.launch {
            getAllReviewsUseCase.invoke(placeId)
                .onSuccess {
                    _reviewsResults.emit(reviewMapper.toUiModel(it))
                }
        }
    }

    fun sendReview(placeId: Long, rating: String, text: String) {
        viewModelScope.launch {
            sendPlaceReviewUseCase.invoke(placeId, rating, text)
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