package com.nyangzzi.withconimal.domain.usecase

import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.repository.DataBaseRepository
import javax.inject.Inject

class DeleteFavoriteAnimalUseCase @Inject constructor(
    private val repository: DataBaseRepository
) {
    suspend operator fun invoke(animalInfo: AnimalInfo) = repository.deleteAnimal(animal = animalInfo)
}