package com.nyangzzi.withconimal.presentation.feature.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyangzzi.withconimal.data.network.ResultWrapper
import com.nyangzzi.withconimal.domain.usecase.SearchAnimalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val searchAnimalUseCase: SearchAnimalUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState

    init {
        viewModelScope.launch {
            searchAnimal()
        }
    }

    fun onEvent(event: FeedEvent) {
        when (event) {
            is FeedEvent.UpdateSelectIndex -> updateSelectIndex(selectedIndex = event.index)
        }
    }

    private fun updateSelectIndex(selectedIndex: Int?) {
        _uiState.update {
            it.copy(selectedIndex = selectedIndex)
        }
    }

    private suspend fun searchAnimal() {
        searchAnimalUseCase(request = uiState.value.request, pageNo = 1).collect { result ->
            Log.d("?", result.toString())
            when (result) {
                is ResultWrapper.Error -> {}
                ResultWrapper.Loading -> {}
                ResultWrapper.None -> {}
                is ResultWrapper.Success -> {
                    _uiState.update { it.copy(animals = result.data) }
                }
            }

        }
    }

}