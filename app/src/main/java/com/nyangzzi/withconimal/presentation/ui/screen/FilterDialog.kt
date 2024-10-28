package com.nyangzzi.withconimal.presentation.ui.screen

import android.app.Dialog
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nyangzzi.withconimal.R
import com.nyangzzi.withconimal.domain.model.common.CareRoomInfo
import com.nyangzzi.withconimal.domain.model.common.CityInfo
import com.nyangzzi.withconimal.domain.model.common.KindType
import com.nyangzzi.withconimal.domain.model.common.NeuterType
import com.nyangzzi.withconimal.domain.model.common.NotificationStateType
import com.nyangzzi.withconimal.domain.model.common.TownInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest
import com.nyangzzi.withconimal.presentation.feature.feed.FeedEvent
import com.nyangzzi.withconimal.presentation.feature.feed.FeedUiState
import com.nyangzzi.withconimal.presentation.ui.component.DateButton
import com.nyangzzi.withconimal.presentation.ui.component.DropDown
import com.nyangzzi.withconimal.presentation.ui.component.DropDownItem
import com.nyangzzi.withconimal.ui.theme.WithconimalTheme
import kotlinx.coroutines.launch
import java.util.logging.Filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    isShown: Boolean,
    searchAnimalRequest: SearchAnimalRequest,
    onConfirm: (SearchAnimalRequest) -> Unit,
    selectCnt: Int,
    uiState: FeedUiState,
    onEvent: (FeedEvent) -> Unit,
    onDismiss: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (isShown) {

        var request by remember {
            mutableStateOf(SearchAnimalRequest())
        }

        var isEnabled by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = searchAnimalRequest) {
            request = searchAnimalRequest
        }

        LaunchedEffect(key1 = request) {
            isEnabled =
                searchAnimalRequest != request && !(request.bgnde != null && request.endde != null && request.bgnde!! > request.endde!!)
        }

        LaunchedEffect(key1 = request.uprCd) {
            if (request.uprCd == null) {
                request = request.copy(orgCd = null)
                onEvent(FeedEvent.GetTownList(uprCd = null))
            } else {
                onEvent(FeedEvent.GetTownList(uprCd = request.uprCd!!))
            }

        }

        LaunchedEffect(key1 = request.orgCd) {
            if (request.orgCd == null || request.uprCd == null) {
                request = request.copy(careRegNo = null)
                onEvent(FeedEvent.GetCareRoomList(uprCd = null, orgCd = null))
            } else {
                onEvent(FeedEvent.GetCareRoomList(uprCd = request.uprCd!!, orgCd = request.orgCd!!))
            }
        }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
        ) {
            FilterContent(
                isEnabled = isEnabled,
                request = request,
                selectCnt = selectCnt,
                uiState = uiState,
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
                        }
                    }
                },
                onClear = {
                    request = SearchAnimalRequest()
                },
                onConfirm = {
                    onConfirm(request)
                },
                setRequest = {
                    request = it
                }
            )
        }
    }

}

@Composable
private fun FilterContent(
    isEnabled: Boolean,
    request: SearchAnimalRequest,
    selectCnt: Int,
    uiState: FeedUiState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onClear: () -> Unit,
    setRequest: (SearchAnimalRequest) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 4.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "필요한 조건이 있나요?",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight(800),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, end = 4.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                RefreshBtn(onClear = onClear)
            }

            FilterKind(
                kind = request.upkind,
                setKind = { setRequest(request.copy(upkind = it)) }
            )
            uiState.cityList?.let {
                FilterArea(
                    cityList = uiState.cityList,
                    uprCd = request.uprCd,
                    setUprCd = { setRequest(request.copy(uprCd = it)) },
                    townList = uiState.townList ?: emptyList(),
                    orgCd = request.orgCd,
                    setOrgCd = { setRequest(request.copy(orgCd = it)) },
                )
            }

            var isCareRoom by remember {
                mutableStateOf(false)
            }
            
            LaunchedEffect(key1 = uiState.careRoomList) {
                isCareRoom = uiState.careRoomList?.isNotEmpty() ?: false
            }
            
            uiState.careRoomList?.let {
                Box(modifier = Modifier
                    .animateContentSize()
                    .heightIn(min = 0.dp)
                    .let {
                        if (isCareRoom) it else it.height(0.dp)
                    }) {
                    FilterCare(
                        careRoomList = uiState.careRoomList,
                        careRegNo = request.careRegNo,
                        setCareRegNo = { setRequest(request.copy(careRegNo = it)) },
                    )
                }
            }
            FilterDate(
                bgnde = request.bgnde,
                setBgnde = { setRequest(request.copy(bgnde = it)) },
                endde = request.endde,
                setEndde = { setRequest(request.copy(endde = it)) },
            )
            FilterState(
                state = request.state,
                setState = { setRequest(request.copy(state = it)) }
            )
            FilterNeuter(
                neuter = request.neuterYn,
                setNeuter = { setRequest(request.copy(neuterYn = it)) }
            )
        }

        BottomBtn(isEnabled = isEnabled, onConfirm = onConfirm, onDismiss = onDismiss)
    }
}

@Composable
private fun BottomBtn(
    isEnabled: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                onDismiss()
            }) {

            Text(
                text = "취소",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

        }

        Button(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onSecondary,
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                onConfirm()
                onDismiss()
            }) {

            Text(
                text = "적용 하기",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

        }
    }
}

@Composable
private inline fun FilterParent(
    @DrawableRes icon: Int? = null,
    title: String,
    isIconTint: Boolean,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            icon?.let {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = it),
                    tint = if (isIconTint) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    contentDescription = ""
                )
            }

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        content()
    }
}

@Composable
private fun FilterKind(
    kind: String?,
    setKind: (String?) -> Unit
) {
    FilterParent(icon = R.drawable.ic_paw_fill, title = "종류", isIconTint = kind != null) {

        var isExpanded by remember {
            mutableStateOf(false)
        }

        DropDown(
            text = "${KindType.entries.firstOrNull { it.code == kind }?.text}",
            isExpanded = isExpanded,
            onClick = {
                isExpanded = !isExpanded
            }
        ) {
            KindType.entries.map {
                DropDownItem(
                    onItemClick = { setKind(it.code) },
                    onDismiss = { isExpanded = false },
                    text = it.text
                )
            }
        }
    }
}

@Composable
private fun FilterState(
    state: String?,
    setState: (String?) -> Unit
) {
    FilterParent(icon = R.drawable.ic_check_process, title = "공고 상태", isIconTint = state != null) {
        var isExpanded by remember {
            mutableStateOf(false)
        }

        DropDown(
            text = "${NotificationStateType.entries.firstOrNull { it.code == state }?.text}",
            isExpanded = isExpanded,
            onClick = {
                isExpanded = !isExpanded
            }
        ) {
            NotificationStateType.entries.map {
                DropDownItem(
                    onItemClick = { setState(it.code) },
                    onDismiss = { isExpanded = false },
                    text = it.text
                )
            }
        }
    }
}

@Composable
private fun FilterDate(
    bgnde: String?,
    setBgnde: (String?) -> Unit,
    endde: String?,
    setEndde: (String?) -> Unit,
) {
    var error by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(bgnde, endde) {
        error = bgnde != null && endde != null && bgnde > endde
    }

    FilterParent(
        icon = R.drawable.ic_calendar,
        title = "날짜",
        isIconTint = bgnde != null || endde != null
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val modifier = Modifier.weight(1f)
            Box(modifier = modifier) {
                DateButton(text = bgnde, onClick = {
                    setBgnde(it)
                })
            }

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_tilde),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )

            Box(modifier = modifier) {
                DateButton(text = endde, onClick = {
                    setEndde(it)
                })
            }
        }
        if (error) {
            Text(
                "* 선택된 날짜를 확인해 주세요 (시작일 ≤ 종료일)",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun FilterArea(
    cityList: List<CityInfo>,
    uprCd: String?,
    setUprCd: (String?) -> Unit,
    townList: List<TownInfo>,
    orgCd: String?,
    setOrgCd: (String?) -> Unit
) {
    FilterParent(
        icon = R.drawable.ic_map_point,
        title = "지역",
        isIconTint = uprCd != null
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            Box(modifier = Modifier.weight(1f)) {
                val cityAll by remember {
                    mutableStateOf(listOf(CityInfo(orgCd = null, orgdownNm = "시/도")) + cityList)
                }
                var isCityExpanded by remember {
                    mutableStateOf(false)
                }

                DropDown(
                    text = "${cityAll.firstOrNull { it.orgCd == uprCd }?.orgdownNm}",
                    isExpanded = isCityExpanded,
                    onClick = {
                        isCityExpanded = !isCityExpanded
                    }
                ) {
                    cityAll.map {
                        DropDownItem(
                            onItemClick = { setUprCd(it.orgCd) },
                            onDismiss = { isCityExpanded = false },
                            text = if (it.orgdownNm == null || it.orgdownNm == "시/도") "전체" else it.orgdownNm
                        )
                    }
                }

            }

            Box(modifier = Modifier.weight(1f)) {
                val townAll = listOf(
                    TownInfo(
                        orgCd = null,
                        uprCd = null,
                        orgdownNm = "시/군/구"
                    )
                ) + townList

                var isTownExpanded by remember {
                    mutableStateOf(false)
                }

                DropDown(
                    text = "${townAll.firstOrNull { it.orgCd == orgCd }?.orgdownNm}",
                    isExpanded = isTownExpanded,
                    isEnabled = uprCd != null && townList.isNotEmpty(),
                    onClick = {
                        isTownExpanded = !isTownExpanded
                    }
                ) {
                    townAll.map {
                        DropDownItem(
                            onItemClick = { setOrgCd(it.orgCd) },
                            onDismiss = { isTownExpanded = false },
                            text = if (it.orgdownNm == null || it.orgdownNm == "시/군/구") "전체" else it.orgdownNm
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterCare(
    careRoomList: List<CareRoomInfo>,
    careRegNo: String?,
    setCareRegNo: (String?) -> Unit
) {
    FilterParent(
        icon = R.drawable.ic_house,
        title = "보호소",
        isIconTint = careRegNo != null
    ) {
        val careRoomAll = listOf(
            CareRoomInfo(
                careRegNo = null,
                careNm = "전체"
            )
        ) + careRoomList

        var isTownExpanded by remember {
            mutableStateOf(false)
        }

        DropDown(
            text = "${careRoomAll.firstOrNull { it.careRegNo == careRegNo }?.careNm}",
            isExpanded = isTownExpanded,
            onClick = {
                isTownExpanded = !isTownExpanded
            }
        ) {
            careRoomAll.map {
                DropDownItem(
                    onItemClick = { setCareRegNo(it.careRegNo) },
                    onDismiss = { isTownExpanded = false },
                    text = it.careNm ?: "전체"
                )
            }
        }
    }
}

@Composable
private fun FilterNeuter(
    neuter: String?,
    setNeuter: (String?) -> Unit
) {
    FilterParent(
        icon = R.drawable.ic_adhesive_plaster,
        title = "중성화 여부",
        isIconTint = neuter != null
    ) {

        var isExpanded by remember {
            mutableStateOf(false)
        }

        DropDown(
            text = "${NeuterType.entries.firstOrNull { it.code == neuter }?.text}",
            isExpanded = isExpanded,
            onClick = {
                isExpanded = !isExpanded
            }
        ) {
            NeuterType.entries.map {
                DropDownItem(
                    onItemClick = { setNeuter(it.code) },
                    onDismiss = { isExpanded = false },
                    text = it.text
                )
            }
        }
    }
}

@Composable
private fun RefreshBtn(onClear: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClear() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(id = R.drawable.ic_refresh),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "필터 초기화",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun ContentPreview() {
    WithconimalTheme {
        FilterContent(
            isEnabled = false,
            request = SearchAnimalRequest(),
            uiState = FeedUiState(),
            selectCnt = 0,
            onClear = {},
            setRequest = {},
            onConfirm = {},
            onDismiss = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun ContentPreviewDark() {
    WithconimalTheme(darkTheme = true) {
        FilterContent(
            isEnabled = true,
            request = SearchAnimalRequest(),
            selectCnt = 0,
            onClear = {},
            uiState = FeedUiState(),
            setRequest = {},
            onConfirm = {},
            onDismiss = {})
    }
}

