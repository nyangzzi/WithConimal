package com.nyangzzi.withconimal.domain.usecase

import com.nyangzzi.withconimal.domain.repository.DataBaseRepository
import javax.inject.Inject

class GetFavoriteAnimalUseCase @Inject constructor(
    private val repository: DataBaseRepository
) {
    operator fun invoke() = repository.getAllAnimals()
}