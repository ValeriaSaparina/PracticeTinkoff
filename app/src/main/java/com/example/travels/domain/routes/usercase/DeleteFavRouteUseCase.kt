package com.example.travels.domain.routes.usercase

import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.ui.routes.model.RouteUIModel
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class DeleteFavRouteUseCase @Inject constructor(
    private val repository: RoutesRepository
) {
    suspend operator fun invoke(route: RouteUIModel) {
        runSuspendCatching {
            repository.deleteFavRoute(route.id)
        }
    }
}
