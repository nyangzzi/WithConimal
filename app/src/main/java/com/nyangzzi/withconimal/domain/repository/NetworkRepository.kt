package com.nyangzzi.withconimal.domain.repository

import com.nyangzzi.withconimal.data.network.ResultWrapper
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    suspend fun searchAnimal(request: SearchAnimalRequest, pageNo: Int): Flow<ResultWrapper<Pair<Int,List<AnimalInfo>>>>
}