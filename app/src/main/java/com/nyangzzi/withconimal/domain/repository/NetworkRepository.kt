package com.nyangzzi.withconimal.domain.repository

import com.nyangzzi.withconimal.data.network.ResultWrapper
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun searchAnimal(): Flow<ResultWrapper<List<AnimalInfo>>>
}