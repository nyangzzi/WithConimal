package com.nyangzzi.withconimal.domain.usecase

import com.nyangzzi.withconimal.domain.repository.NetworkRepository
import javax.inject.Inject

class GetTownListUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
){
    suspend operator fun invoke(uprCd: String) = networkRepository.getTown(uprCd = uprCd)
}