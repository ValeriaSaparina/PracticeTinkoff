package com.example.travels.domain.routes.usercase

import com.example.travels.data.routes.mapper.RouteDomainMapper
import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class SearchRoutesUseCase @Inject constructor(
    private val repository: RoutesRepository,
    private val mapper: RouteDomainMapper,
) {
    suspend operator fun invoke(query: String): Result<List<RouteDomainModel>> {
        return runSuspendCatching {
            val favorites = repository.getIdAllFavRoutes()
            repository.searchRoutes(query).map {
                mapper.toDomainModel(it).copy(
                    isFav = favorites.contains(it.id)
                )
            }
        }
    }
}
