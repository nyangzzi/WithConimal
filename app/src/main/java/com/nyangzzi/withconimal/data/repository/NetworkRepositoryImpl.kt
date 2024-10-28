package com.nyangzzi.withconimal.data.repository

import android.util.Log
import com.nyangzzi.withconimal.BuildConfig
import com.nyangzzi.withconimal.data.network.ResultWrapper
import com.nyangzzi.withconimal.data.service.SearchAnimalManager
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.common.CareRoomInfo
import com.nyangzzi.withconimal.domain.model.common.CityInfo
import com.nyangzzi.withconimal.domain.model.common.TownInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest
import com.nyangzzi.withconimal.domain.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class NetworkRepositoryImpl : NetworkRepository {
    override suspend fun searchAnimal(
        request: SearchAnimalRequest,
        pageNo: Int
    ): Flow<ResultWrapper<Pair<Int, List<AnimalInfo>>>> = flow {
        SearchAnimalManager.getService().getAnimalList(
            serviceKey = BuildConfig.common_api_key,
            bgnde = request.bgnde,
            endde = request.endde,
            upkind = request.upkind,
            kind = request.kind,
            uprCd = request.uprCd,
            orgCd = request.orgCd,
            careRegNo = request.careRegNo,
            state = request.state,
            neuterYn = request.neuterYn,
            pageNo = pageNo,
            numOfRows = 20,
            type = "json",
        )
            .onSuccess {
                emit(
                    ResultWrapper.Success(
                        Pair(
                            it.response.body?.totalCount ?: 0,
                            it.response.body?.items?.item ?: emptyList()
                        )
                    )
                )
            }.onFailure {
                Log.d("animal list", it.message ?: "")
                emit(ResultWrapper.Error(it.message ?: "error: Failure Get Animal List"))
            }

    }.flowOn(Dispatchers.IO)

    override suspend fun getCity(): Flow<ResultWrapper<List<CityInfo>>> = flow {
        SearchAnimalManager.getService().getCity(
            serviceKey = BuildConfig.common_api_key,
            numOfRows = 1000,
            pageNo = 1,
            type = "json",
        )
            .onSuccess {
                emit(
                    ResultWrapper.Success(
                        it.response.body?.items?.item ?: emptyList()
                    )
                )
            }.onFailure {
                Log.d("city list", it.message ?: "")
                emit(ResultWrapper.Error(it.message ?: "error: Failure Get City List"))
            }

    }.flowOn(Dispatchers.IO)

    override suspend fun getTown(uprCd: String): Flow<ResultWrapper<List<TownInfo>>> = flow {
        SearchAnimalManager.getService().getTown(
            serviceKey = BuildConfig.common_api_key,
            uprCd = uprCd,
            type = "json",
        )
            .onSuccess {
                emit(
                    ResultWrapper.Success(
                        it.response.body?.items?.item ?: emptyList()
                    )
                )
            }.onFailure {
                Log.d("town list", it.message ?: "")
                emit(ResultWrapper.Error(it.message ?: "error: Failure Get Town List"))
            }

    }.flowOn(Dispatchers.IO)


    override suspend fun getCareRoom(
        uprCd: String,
        orgCd: String
    ): Flow<ResultWrapper<List<CareRoomInfo>>> = flow {
        SearchAnimalManager.getService().getCareRoom(
            serviceKey = BuildConfig.common_api_key,
            uprCd = uprCd,
            orgCd = orgCd,
            type = "json",
        )
            .onSuccess {
                emit(
                    ResultWrapper.Success(
                        it.response.body?.items?.item ?: emptyList()
                    )
                )
            }.onFailure {
                Log.d("care room list", it.message ?: "")
                emit(ResultWrapper.Error(it.message ?: "error: Failure Get Care Room List"))
            }

    }.flowOn(Dispatchers.IO)


}