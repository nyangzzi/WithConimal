package com.nyangzzi.withconimal.domain.usecase

import com.nyangzzi.withconimal.domain.repository.NetworkRepository
import javax.inject.Inject

class GetCityListUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
){
    suspend operator fun invoke() = networkRepository.getCity()
}