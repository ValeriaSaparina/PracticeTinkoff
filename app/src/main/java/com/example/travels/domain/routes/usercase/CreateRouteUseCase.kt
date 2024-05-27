package com.example.travels.domain.routes.usercase

import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.ui.places.model.PlaceUiModel
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class CreateRouteUseCase @Inject constructor(
    private val routesRepository: RoutesRepository,
) {
    suspend operator fun invoke(
        name: String,
        type: String,
        places: MutableList<PlaceUiModel>
    ): Result<Unit> {
        return runSuspendCatching {
            routesRepository.createRoute(name, type, places)
        }
    }
}
