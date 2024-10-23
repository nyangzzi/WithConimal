package com.nyangzzi.withconimal.data.service

import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.network.NetworkModel
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAnimalService {
    @GET("abandonmentPublic?")
    suspend fun getAnimalList(
        @Query("serviceKey") serviceKey: String,
        @Query("bgnde") bgnde: String?,
        @Query("endde") endde: String?,
        @Query("upkind") upkind: String?,
        @Query("kind") kind: String?,
        @Query("upr_cd") uprCd: String?,
        @Query("org_cd") orgCd: String?,
        @Query("care_reg_no") careRegNo: String?,
        @Query("state") state: String?,
        @Query("neuter_yn") neuterYn: String?,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("_type") type: String,
    ): Result<NetworkModel<AnimalInfo>>

}