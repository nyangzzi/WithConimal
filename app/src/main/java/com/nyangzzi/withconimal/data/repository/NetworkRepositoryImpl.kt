package com.nyangzzi.withconimal.data.repository

import android.util.Log
import com.nyangzzi.withconimal.BuildConfig
import com.nyangzzi.withconimal.data.network.ResultWrapper
import com.nyangzzi.withconimal.data.service.SearchAnimalManager
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class NetworkRepositoryImpl : NetworkRepository {
    override fun searchAnimal(): Flow<ResultWrapper<List<AnimalInfo>>> = flow {
        SearchAnimalManager.getService().getAnimalList(
            serviceKey = BuildConfig.common_api_key,
            bgnde = "",
            endde = "",
            upkind = "417000",
            kind = "",
            uprCd = "",
            orgCd = "",
            careRegNo = "",
            state = "",
            neuterYn = "",
            pageNo = 1,
            numOfRows = 10,
            type = "json",
            )
            .onSuccess {
                Log.d("animal list", it.toString() ?: "")
                emit(ResultWrapper.Success(it.response.body?.items?.item ?: emptyList()))
            }.onFailure {
                Log.d("animal list", it.message ?: "")
                emit(ResultWrapper.Error(it.message ?: "error: Failure Get Animal List"))
            }

    }.onStart { emit(ResultWrapper.Loading) }.flowOn(Dispatchers.IO)
}