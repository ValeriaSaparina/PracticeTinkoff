package com.example.travels.domain.routes

import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class UpdateRouteUseCase @Inject constructor(
    private val routesRepository: RoutesRepository,
) {
    suspend operator fun invoke(route: RouteDomainModel): Result<Unit> {
        return runSuspendCatching {
            routesRepository.updateRoute(route)
        }
    }
}
