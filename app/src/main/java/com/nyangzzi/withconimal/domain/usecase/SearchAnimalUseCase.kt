package com.nyangzzi.withconimal.domain.usecase

import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest
import com.nyangzzi.withconimal.domain.repository.NetworkRepository
import javax.inject.Inject

class SearchAnimalUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
){
    operator fun invoke(request: SearchAnimalRequest, pageNo: Int) = networkRepository.searchAnimal(request = request, pageNo = pageNo)
}