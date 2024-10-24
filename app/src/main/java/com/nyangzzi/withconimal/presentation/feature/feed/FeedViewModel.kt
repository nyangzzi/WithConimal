package com.nyangzzi.withconimal.presentation.feature.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.nyangzzi.withconimal.data.network.ResultWrapper
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest
import com.nyangzzi.withconimal.domain.usecase.SearchAnimalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val searchAnimalUseCase: SearchAnimalUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState

    val animalPagingFlow = Pager(PagingConfig(pageSize = 20)) {
        val result = AnimalPagingSource(
            searchAnimalUseCase,
            initialRequest = SearchAnimalRequest(),
            totalCnt = { cnt ->
                _uiState.update { it.copy(totalCnt = cnt) }
            })
        result
    }.flow.cachedIn(viewModelScope)

    fun onEvent(event: FeedEvent) {
        when (event) {
            is FeedEvent.UpdateSelectInfo -> updateSelectData(selectData = event.data)
        }
    }

    private fun updateSelectData(selectData: AnimalInfo?) {
        _uiState.update {
            it.copy(selectData = selectData)
        }
    }

}