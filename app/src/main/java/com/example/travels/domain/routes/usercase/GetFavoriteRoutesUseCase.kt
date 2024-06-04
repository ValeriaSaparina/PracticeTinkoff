package com.example.travels.domain.routes.usercase

import com.example.travels.domain.routes.model.RouteDomainModel
import com.example.travels.domain.routes.repository.RoutesRepository
import com.example.travels.utils.runSuspendCatching
import javax.inject.Inject

class GetFavoriteRoutesUseCase @Inject constructor(
    private val repository: RoutesRepository
) {
    suspend operator fun invoke(n: Int? = null): Result<List<RouteDomainModel>> {
        return runSuspendCatching {
            if (n == null) {
                repository.getAllFavRoutes()
            } else {
                repository.getFavRoutes(n)
            }
        }
    }
}
