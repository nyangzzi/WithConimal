package com.nyangzzi.withconimal.presentation.feature.feed

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nyangzzi.withconimal.data.network.ResultWrapper
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest
import com.nyangzzi.withconimal.domain.usecase.SearchAnimalUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class AnimalPagingSource(
    private val searchAnimalUseCase: SearchAnimalUseCase,
    private val initialRequest: SearchAnimalRequest,
    val totalCnt: (Int)->Unit,
) : PagingSource<Int, AnimalInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimalInfo> {
        val currentPage = params.key ?: 1

        return try {
            // 서버에서 데이터를 가져옴
            when (val result = searchAnimalUseCase(initialRequest, currentPage).first()) {
                is ResultWrapper.Success -> {
                    val currentLoadedItems =
                        (currentPage - 1) * params.loadSize + result.data.second.size

                    totalCnt(result.data.first)

                    LoadResult.Page(
                        data = result.data.second,  // 서버에서 받은 동물 데이터 리스트
                        prevKey = if (currentPage == 1) null else currentPage - 1,  // 첫 페이지는 이전 페이지가 없음
                        nextKey = if (currentLoadedItems >= result.data.first || result.data.second.isEmpty()) null else currentPage + 1  // 더 이상 데이터가 없으면 다음 페이지 없음
                    )
                }

                is ResultWrapper.Error -> {
                    LoadResult.Error(Throwable("Error fetching data"))
                }

                else -> {
                    LoadResult.Error(Throwable("Unexpected result"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimalInfo>): Int? {
        // 현재 데이터의 중간 위치를 기준으로 새로고침 시 사용할 키를 반환
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}


