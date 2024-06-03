package com.example.travels.domain.routes.usercase

import com.example.travels.data.routes.mapper.RouteDomainMapper
import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.utils.runSuspendCatching
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class GetUserRoutesUseCase @Inject constructor(
    private val routesRepository: RoutesRepository,
    private val mapper: RouteDomainMapper,
    private val auth: FirebaseAuth
) {
    suspend operator fun invoke(id: String = auth.uid!!): Result<List<RouteDomainModel>> {
        return runSuspendCatching {
            routesRepository.getUserRoutes(id).map { mapper.toDomainModel(it) }
        }
    }
}
