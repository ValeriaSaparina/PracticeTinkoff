package com.example.travels.domain.routes.usercase

import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.ui.mapper.RoutesUiModelMapper
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class AddNewFavRouteUseCase @Inject constructor(
    private val repository: RoutesRepository,
    private val mapper: RoutesUiModelMapper
) {
    suspend operator fun invoke(route: RouteUIModel) {
        runSuspendCatching {
            repository.addNewFavRoute(mapper.toDomainModel(route))
        }
    }
}