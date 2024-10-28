package com.nyangzzi.withconimal.domain.usecase

import com.nyangzzi.withconimal.domain.repository.NetworkRepository
import javax.inject.Inject

class GetCareRoomListUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(uprCd: String, orgCd: String) =
        networkRepository.getCareRoom(uprCd = uprCd, orgCd = orgCd)
}