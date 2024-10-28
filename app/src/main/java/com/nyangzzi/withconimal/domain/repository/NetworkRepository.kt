package com.nyangzzi.withconimal.domain.repository

import com.nyangzzi.withconimal.data.network.ResultWrapper
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.common.CareRoomInfo
import com.nyangzzi.withconimal.domain.model.common.CityInfo
import com.nyangzzi.withconimal.domain.model.common.TownInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    suspend fun searchAnimal(request: SearchAnimalRequest, pageNo: Int): Flow<ResultWrapper<Pair<Int,List<AnimalInfo>>>>
    suspend fun getCity(): Flow<ResultWrapper<List<CityInfo>>>
    suspend fun getTown(uprCd: String): Flow<ResultWrapper<List<TownInfo>>>
    suspend fun getCareRoom(uprCd: String, orgCd: String): Flow<ResultWrapper<List<CareRoomInfo>>>

}