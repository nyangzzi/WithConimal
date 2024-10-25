package com.nyangzzi.withconimal.presentation.ui.screen

import android.app.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.nyangzzi.withconimal.domain.model.common.NeuterType
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest
import com.nyangzzi.withconimal.ui.theme.WithconimalTheme
import java.util.logging.Filter

@Composable
fun FilterDialog(
    isShown: Boolean,
    searchAnimalRequest: SearchAnimalRequest,
    onDismiss: () -> Unit
) {

    if (isShown) {

        var request by remember {
            mutableStateOf(SearchAnimalRequest())
        }
        var selectCnt by remember {
            mutableStateOf(0)
        }

        LaunchedEffect(key1 = searchAnimalRequest) {
            request = searchAnimalRequest
        }

        LaunchedEffect(key1 = request) {
            selectCnt = listOf(
                request.neuterYn,
                request.kind,
                request.upkind,
                request.bgnde,
                request.endde,
                request.careRegNo,
                request.state,
                request.uprCd
            ).count { it != null }
        }


        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            FilterContent(
                request = request,
                selectCnt = selectCnt,
                onDismiss = onDismiss, onClear = {
                    request = SearchAnimalRequest()
                })
        }
    }

}

@Composable
private fun FilterContent(
    request: SearchAnimalRequest,
    selectCnt: Int,
    onDismiss: () -> Unit,
    onClear: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(vertical = 16.dp, horizontal = 4.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .clip(shape = CircleShape)
                    .clickable { onDismiss() }
                    .padding(12.dp),
                tint = MaterialTheme.colorScheme.onSurface,
                painter = painterResource(id = R.drawable.ic_left_line),
                contentDescription = ""
            )

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
                .padding(18.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, end = 4.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectCnt}개 선택",
                    modifier = Modifier.padding(start = 18.dp).weight(1f),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                RefreshBtn(onClear = onClear)
            }

            FilterKind()
            FilterNeuter(neuter = request.neuterYn, setNeuter = {})
            FilterState()
            FilterDate()
            FilterArea()
            FilterCare()
        }

        Button(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onSecondary,
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                //todo
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
    title: String,
    particles: String,
    content: @Composable () -> Unit,

    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "$particles 선택해 주세요",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Box(modifier = Modifier.size(60.dp))
        content()
    }
}

@Composable
private fun FilterKind() {
    FilterParent(title = "종류", particles = "를") {

    }
}

@Composable
private fun FilterState() {
    FilterParent(title = "공고 상태", particles = "를") {

    }
}

@Composable
private fun FilterDate() {
    FilterParent(title = "날짜", particles = "를") {

    }
}

@Composable
private fun FilterArea() {
    FilterParent(title = "지역", particles = "을") {

    }
}

@Composable
private fun FilterCare() {
    FilterParent(title = "보호소", particles = "를") {

    }
}

@Composable
private fun FilterNeuter(
    neuter: String?,
    setNeuter: (String?) -> Unit
) {
    FilterParent(title = "중성화 여부", particles = "를") {
        NeuterType.entries.firstOrNull() { it.code == neuter }
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
        FilterContent(request = SearchAnimalRequest(), selectCnt = 0, onClear = {}, onDismiss = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreviewDark() {
    WithconimalTheme(darkTheme = true) {
        FilterContent(request = SearchAnimalRequest(), selectCnt = 0, onClear = {}, onDismiss = {})
    }
}

