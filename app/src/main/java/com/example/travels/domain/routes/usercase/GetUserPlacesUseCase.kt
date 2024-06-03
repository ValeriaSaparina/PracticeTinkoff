package com.example.travels.domain.routes.usercase

import com.example.travels.data.routes.mapper.RouteDomainMapper
import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetUserPlacesUseCase @Inject constructor(
    private val routesRepository: RoutesRepository,
    private val mapper: RouteDomainMapper,
) {
    suspend operator fun invoke(id: String): Result<List<RouteDomainModel>> {
        return runSuspendCatching {
            routesRepository.getUserRoutes(id).map { mapper.toDomainModel(it) }
        }
    }
}
