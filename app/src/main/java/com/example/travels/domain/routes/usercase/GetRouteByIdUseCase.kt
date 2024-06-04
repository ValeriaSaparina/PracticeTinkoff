package com.example.travels.domain.routes.usercase

import com.example.travels.data.routes.mapper.RouteDomainMapper
import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetRouteByIdUseCase @Inject constructor(
    private val repository: RoutesRepository,
    private val mapper: RouteDomainMapper,
) {
    suspend operator fun invoke(id: String): Result<RouteDomainModel> {
        return runSuspendCatching {
            mapper.toDomainModel(repository.getRoute(id)!!)
        }
    }
}