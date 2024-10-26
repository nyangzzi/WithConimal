package com.nyangzzi.withconimal.domain.repository

import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.db.FavoriteAnimal
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {
    suspend fun addAnimal(animal: AnimalInfo)
    fun getAllAnimals(): Flow<List<AnimalInfo>> // Flow 반환 타입
    suspend fun updateAnimal(animal: AnimalInfo)
    suspend fun deleteAnimal(animal: AnimalInfo)
}