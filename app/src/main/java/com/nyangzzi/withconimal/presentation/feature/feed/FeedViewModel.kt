package com.nyangzzi.withconimal.presentation.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.nyangzzi.withconimal.data.network.ResultWrapper
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest
import com.nyangzzi.withconimal.domain.usecase.AddFavoriteAnimalUseCase
import com.nyangzzi.withconimal.domain.usecase.DeleteFavoriteAnimalUseCase
import com.nyangzzi.withconimal.domain.usecase.GetCareRoomListUseCase
import com.nyangzzi.withconimal.domain.usecase.GetCityListUseCase
import com.nyangzzi.withconimal.domain.usecase.GetFavoriteAnimalUseCase
import com.nyangzzi.withconimal.domain.usecase.GetTownListUseCase
import com.nyangzzi.withconimal.domain.usecase.SearchAnimalUseCase
import com.nyangzzi.withconimal.domain.usecase.UpdateFavoriteAnimalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val searchAnimalUseCase: SearchAnimalUseCase,
    private val getFavoriteAnimalUseCase: GetFavoriteAnimalUseCase,
    private val addFavoriteAnimalUseCase: AddFavoriteAnimalUseCase,
    private val deleteFavoriteAnimalUseCase: DeleteFavoriteAnimalUseCase,
    private val getCityListUseCase: GetCityListUseCase,
    private val getTownListUseCase: GetTownListUseCase,
    private val getCareRoomListUseCase: GetCareRoomListUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState

    private val _favoriteAnimal = MutableStateFlow<List<AnimalInfo>>(emptyList())
    val favoriteAnimal: StateFlow<List<AnimalInfo>> = _favoriteAnimal

    var animalPagingFlow = getAnimalPager()

    private fun getAnimalPager() = Pager(PagingConfig(pageSize = 20)) {
        val result = AnimalPagingSource(
            searchAnimalUseCase,
            initialRequest = uiState.value.request,
            totalCnt = { cnt ->
                _uiState.update { it.copy(totalCnt = cnt) }
            })
        result
    }.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            getFavoriteAnimal()
        }
        viewModelScope.launch {
            getCityList()
        }
    }

    fun onEvent(event: FeedEvent) {
        viewModelScope.launch {
            when (event) {
                is FeedEvent.UpdateSelectInfo -> updateSelectData(selectData = event.data)
                is FeedEvent.UpdateRequest -> updateRequest(request = event.request)
                is FeedEvent.SetShowImageExpand -> setShowImageExpand(isShow = event.isShow)
                is FeedEvent.SetShowFilter -> setShowFilter(isShow = event.isShow)
                is FeedEvent.DeleteFavoriteAnimal -> deleteFavoriteAnimal(animalInfo = event.animalInfo)
                is FeedEvent.AddFavoriteAnimal -> addFavoriteAnimal(animalInfo = event.animalInfo)
                is FeedEvent.GetAnimalList -> animalPagingFlow = getAnimalPager()
                is FeedEvent.GetCareRoomList -> getCareRoomList(
                    uprCd = event.uprCd,
                    orgCd = event.orgCd
                )

                is FeedEvent.GetCityList -> getCityList()
                is FeedEvent.GetTownList -> getTownList(uprCd = event.uprCd)
            }
        }
    }

    private fun updateRequest(request: SearchAnimalRequest) {
        _uiState.update {
            it.copy(request = request,
                totalCnt = 0,
                selectCnt = listOf(
                    request.neuterYn,
                    request.kind,
                    request.upkind,
                    if (request.bgnde == null && request.endde == null) null else "",
                    request.careRegNo,
                    request.state,
                    request.uprCd
                ).count { cnt -> cnt != null })
        }
    }

    private suspend fun getCityList() {
        getCityListUseCase().collect { result ->
            when (result) {
                is ResultWrapper.Success -> _uiState.update { it.copy(cityList = result.data) }
                else -> {}
            }
        }
    }

    private suspend fun getTownList(uprCd: String?) {
        if (uprCd == null) {
            _uiState.update { it.copy(townList = null, careRoomList = null) }
        } else {
            _uiState.update { it.copy(townList = null, careRoomList = null) }
            getTownListUseCase(uprCd = uprCd).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> _uiState.update { it.copy(townList = result.data) }
                    else -> {}
                }
            }
        }
    }

    private suspend fun getCareRoomList(uprCd: String?, orgCd: String?) {
        if (uprCd == null || orgCd == null) {
            _uiState.update { it.copy(careRoomList = null) }
        } else {
            _uiState.update { it.copy(careRoomList = null) }
            getCareRoomListUseCase(uprCd = uprCd, orgCd = orgCd).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> _uiState.update { it.copy(careRoomList = result.data) }
                    else -> {}
                }
            }
        }
    }

    private suspend fun getFavoriteAnimal() {
        getFavoriteAnimalUseCase().collect { animals ->
            _favoriteAnimal.value = animals
        }
    }

    private suspend fun addFavoriteAnimal(animalInfo: AnimalInfo) {
        addFavoriteAnimalUseCase(animalInfo = animalInfo)
    }

    private suspend fun deleteFavoriteAnimal(animalInfo: AnimalInfo) {
        deleteFavoriteAnimalUseCase(animalInfo = animalInfo)
    }

    private fun updateSelectData(selectData: AnimalInfo?) {
        _uiState.update {
            it.copy(selectData = selectData)
        }
    }

    private fun setShowImageExpand(isShow: Boolean) {
        _uiState.update {
            it.copy(isShowImageExpand = isShow)
        }
    }

    private fun setShowFilter(isShow: Boolean) {
        _uiState.update {
            it.copy(isShowFilter = isShow)
        }
    }

}