package com.nyangzzi.withconimal.presentation.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nyangzzi.withconimal.R
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.common.NeuterType
import com.nyangzzi.withconimal.presentation.feature.feed.FeedEvent
import com.nyangzzi.withconimal.presentation.feature.feed.FeedViewModel
import com.nyangzzi.withconimal.presentation.util.Utils
import com.nyangzzi.withconimal.presentation.util.noRippleClickable
import com.nyangzzi.withconimal.ui.theme.WithconimalTheme

@Composable
fun DetailScreen(navController: NavHostController, viewModel: FeedViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoriteAnimal by viewModel.favoriteAnimal.collectAsStateWithLifecycle()
    var isFavorite by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = favoriteAnimal) {
        isFavorite = favoriteAnimal.any { it.desertionNo == uiState.selectData?.desertionNo }
    }

    uiState.selectData?.let {
        ImageExpandDialog(
            isShown = uiState.isShowImageExpand,
            imageUrl = it.popfile,
            onDismiss = { viewModel.onEvent(FeedEvent.SetShowImageExpand(false)) })

        DetailContent(info = it,
            isFavorite = isFavorite,
            onClickImage = { viewModel.onEvent(FeedEvent.SetShowImageExpand(true)) },
            onClickFavorite = {
                if (isFavorite) viewModel.onEvent(FeedEvent.DeleteFavoriteAnimal(it))
                else viewModel.onEvent(FeedEvent.AddFavoriteAnimal(it))
            },
            onClickBack = {
                viewModel.onEvent(FeedEvent.UpdateSelectInfo(data = null))
                navController.popBackStack()
            })
    }
}

@Composable
fun DetailContent(
    info: AnimalInfo,
    isFavorite: Boolean,
    onClickImage: () -> Unit = {},
    onClickFavorite: () -> Unit = {},
    onClickBack: () -> Unit = {}
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        val minScroll = 0f
        val maxScroll = 300f

        val scrollState = rememberScrollState()

        val alpha by remember {
            derivedStateOf {
                (scrollState.value / (maxScroll * 2)).coerceIn(0f, 1f) // 0에서 1 사이로 제한
            }
        }
        val animatedAlpha by animateFloatAsState(targetValue = alpha, label = "")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(56.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .clip(shape = CircleShape)
                    .clickable { onClickBack() }
                    .padding(12.dp),
                tint = MaterialTheme.colorScheme.onSurface,
                painter = painterResource(id = R.drawable.ic_left_line),
                contentDescription = ""
            )


            Text(
                modifier = Modifier
                    .weight(1f)
                    .alpha(animatedAlpha),
                text = info.kindCd ?: "",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight(800),
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .clickable { onClickFavorite() }
                    .padding(12.dp)
                    .alpha(animatedAlpha),
                painter = painterResource(id = if (isFavorite) R.drawable.ic_heart_fill else R.drawable.ic_heart_line),
                contentDescription = ""
            )

        }

        val boxHeight by remember {
            derivedStateOf {
                max(minScroll.dp, maxScroll.dp - (scrollState.value / 2).dp)
            }
        }
        val animatedBoxHeight by animateDpAsState(targetValue = boxHeight, label = "")

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(animatedBoxHeight)
        ) {
            AnimalImage(
                isFavorite = isFavorite,
                processState = info.processState,
                onClickImage = onClickImage,
                onClickFavorite = onClickFavorite,
                imageUrl = info.popfile
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {

            DetailInfo(
                specialMark = info.specialMark,
                noticeComment = info.noticeComment,
                kindCd = info.kindCd,
                colorCd = info.colorCd,
                age = info.age,
                weight = info.weight,
                sexCd = info.sexCd,
                neuterYn = info.neuterYn
            )

            MarginContent()

            CareRoomInfo(
                careNm = info.careNm,
                careTel = info.careTel,
                careAddr = info.careAddr,
                orgNm = info.orgNm,
                chargeNm = info.chargeNm,
                officetel = info.officetel
            )

            MarginContent()

            NoticeInfo(
                noticeNo = info.noticeNo,
                noticeSdt = info.noticeSdt,
                noticeEdt = info.noticeEdt,
                processState = info.processState
            )

            MarginContent()

            AdoptionInfo(
                desertionNo = info.desertionNo,
                happenDt = info.happenDt,
                happenPlace = info.happenPlace
            )

        }
    }
}

@Composable
private fun MarginContent() {
    HorizontalDivider(
        thickness = 8.dp,
        color = MaterialTheme.colorScheme.inverseOnSurface
    )
}

@Composable
private fun AnimalImage(
    isFavorite: Boolean,
    onClickFavorite: () -> Unit,
    processState: String?,
    onClickImage: () -> Unit,
    imageUrl: String?
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        imageUrl?.let {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Utils.setImageUrl(imageUrl))
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClickImage() },
                placeholder = painterResource(id = R.drawable.ic_loading_image)
            )
        }

        processState?.let {
            Box(
                modifier = Modifier
                    .padding(22.dp)
                    .background(color = Color(0xAA000000), shape = RoundedCornerShape(20.dp))
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            ) {
                Text(text = it, color = Color.White)
            }
        }

        Icon(
            modifier = Modifier
                .padding(22.dp)
                .align(Alignment.TopEnd)
                .size(18.dp),
            painter = painterResource(id = R.drawable.ic_maximize),
            contentDescription = "",
            tint = Color.White
        )


        Row(
            modifier = Modifier
                .padding(28.dp)
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            val iconModifier = Modifier
                .size(46.dp)
                .clip(shape = CircleShape)

            Icon(
                modifier = iconModifier
                    .clickable { onClickFavorite() }
                    .padding(8.dp),
                painter = painterResource(id = if (isFavorite) R.drawable.ic_heart_fill else R.drawable.ic_heart_line),
                contentDescription = "",
                tint = Color.White
            )

            Icon(
                modifier = iconModifier
                    .clickable { }
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.ic_sharing),
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailInfo(
    specialMark: String?,
    noticeComment: String?,
    kindCd: String?,
    colorCd: String?,
    age: String?,
    weight: String?,
    sexCd: String?,
    neuterYn: String?
) {
    val infoList = listOf(
        FeatureData(
            icon = R.drawable.ic_color,
            title = "색상",
            text = colorCd
        ),
        FeatureData(
            icon = R.drawable.ic_heart_age,
            title = "나이",
            text = age
        ),
        FeatureData(
            icon = R.drawable.ic_weight,
            title = "체중",
            text = weight
        ),
        FeatureData(
            icon = when (sexCd) {
                "M" -> R.drawable.ic_male
                "F" -> R.drawable.ic_female
                else -> R.drawable.ic_male_female
            },
            title = "성별",
            text = when (sexCd) {
                "M" -> "남"
                "F" -> "여"
                else -> "확인불가"
            }
        ),
        FeatureData(
            icon = when (NeuterType.entries.firstOrNull { it.code == neuterYn }) {
                NeuterType.YES -> R.drawable.ic_neuter_yes
                NeuterType.NO -> R.drawable.ic_neuter_no
                else -> R.drawable.ic_neuter_dont
            },
            title = "중성화",
            text = when (NeuterType.entries.firstOrNull { it.code == neuterYn }) {
                NeuterType.YES -> "완료"
                NeuterType.NO -> "미완료"
                else -> "확인불가"
            }
        )
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(
                    id = if (kindCd?.contains("[개]") == true || kindCd?.contains("[강아지]") == true) R.drawable.ic_kind_dog
                    else if (kindCd?.contains("[고양이]") == true) R.drawable.ic_kind_cat
                    else R.drawable.ic_kind_etc,
                ),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                text = kindCd ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        specialMark?.let {
            Text(
                text = it,
                fontSize = 18.sp,
                fontWeight = FontWeight(500),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        noticeComment?.let {
            Text(
                text = it,
                fontSize = 18.sp,
                fontWeight = FontWeight(500),
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            infoList.map {
                FeatureInfo(
                    icon = it.icon,
                    title = it.title,
                    text = it.text
                )
            }

        }
    }


}

data class FeatureData(
    @DrawableRes val icon: Int,
    val title: String,
    val text: String?
)

@Composable
fun FeatureInfo(@DrawableRes icon: Int, title: String, text: String?, iconTint: Color? = null) {
    text?.let {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(8.dp)
                .defaultMinSize(minWidth = 96.dp)
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight(700),
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = iconTint ?: MaterialTheme.colorScheme.outline
            )
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onSurface
            )

        }
    }
}

@Composable
private fun CareRoomInfo(
    careNm: String?,
    careTel: String?,
    careAddr: String?,
    orgNm: String?,
    chargeNm: String?,
    officetel: String?
) {
    InfoParent(
        icon = R.drawable.ic_heart_lock,
        title = "저의 사랑이 되어주세요!",
    ) {
        Text(
            text = "[보호소]",
            fontSize = 20.sp,
            fontWeight = FontWeight(700),
            color = MaterialTheme.colorScheme.onSurface
        )

        TextContent(title = "이름", text = careNm)
        TextContent(title = "전화번호", text = careTel, isTel = true)
        TextContent(title = "보호 장소", text = careAddr)
        TextContent(title = "관할 기관", text = orgNm)

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = "[담당자]",
            fontSize = 20.sp,
            fontWeight = FontWeight(700),
            color = MaterialTheme.colorScheme.onSurface
        )

        TextContent(title = "이름", text = chargeNm)
        TextContent(title = "연락처", text = officetel, isTel = true)
    }
}

@Composable
private fun NoticeInfo(
    noticeNo: String?,
    noticeSdt: String?,
    noticeEdt: String?,
    processState: String?
) {

    InfoParent(
        icon = R.drawable.ic_heart_hand,
        title = "운명적 만남을 기다리고 있어요",
    ) {
        TextContent(title = "공고 번호", text = noticeNo)
        TextContent(
            title = "공고일",
            text = "${Utils.dateFormat(noticeSdt)} ~ ${Utils.dateFormat(noticeEdt)}"
        )
        TextContent(title = "공고 상태", text = processState)
    }

}

@Composable
private fun AdoptionInfo(
    desertionNo: String?,
    happenDt: String?,
    happenPlace: String?
) {
    InfoParent(
        icon = R.drawable.ic_heart_broken,
        title = "혹시 저를 찾고 있나요?",
    ) {
        TextContent(title = "유기 번호", text = desertionNo)
        TextContent(title = "접수일", text = Utils.dateFormat(happenDt))
        TextContent(title = "발견 장소", text = happenPlace)
    }
}

@Composable
private inline fun InfoParent(
    @DrawableRes icon: Int,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {

    var isShown by remember {
        mutableStateOf(true)
    }
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .noRippleClickable {
                    isShown = !isShown
                }
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(
                modifier = Modifier.weight(1f),
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = if (isShown) R.drawable.ic_up_line else R.drawable.ic_down_line),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )

        }

        Column(
            modifier = Modifier
                .animateContentSize()
                .heightIn(min = 0.dp)
                .let {
                    if (isShown) it else it.height(0.dp)
                },
        ) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 32.dp)
                    .padding(bottom = 6.dp)
            ) {
                content()
            }
        }

    }
}

@Composable
private fun TextContent(title: String, text: String?, isTel: Boolean = false) {
    text?.let {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.secondary
            )
            val context = LocalContext.current

            Row(modifier = Modifier
                .noRippleClickable {
                    if (isTel) {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$text")
                        }
                        context.startActivity(intent)
                    }
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                if (isTel) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_call),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary)
                }
                Text(
                    modifier = Modifier.padding(top = 1.dp),
                    text = text,
                    textDecoration = if (isTel) TextDecoration.Underline else null,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = if (isTel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    WithconimalTheme {
        DetailContent(
            isFavorite = false,
            info = AnimalInfo(
                desertionNo = "448548202400918",
                filename = "http://www.animal.go.kr/files/shelter/2024/09/202410231110474_s.jpg",
                happenDt = "20241023",
                happenPlace = "초계면",
                kindCd = "[개] 믹스견",
                colorCd = "갈색",
                age = "2023(년생)",
                weight = "2(Kg)",
                noticeNo = "경남-합천-2024-00433",
                noticeSdt = "20241023",
                noticeEdt = "20241104",
                popfile = "http://www.animal.go.kr/files/shelter/2024/09/202410231110474.jpg",
                processState = "보호중",
                sexCd = "F",
                neuterYn = "N",
                specialMark = "마을배회",
                careNm = "태민동물병원",
                careTel = "010-2868-2108",
                careAddr = "경상남도 합천군 합천읍 옥산로 16 (합천읍, 까치빌라) 태민동물병원",
                orgNm = "경상남도 합천군",
                chargeNm = "고영삼",
                officetel = "055-930-3562"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDark() {
    WithconimalTheme(darkTheme = true) {
        DetailContent(
            isFavorite = true,
            info = AnimalInfo(
                desertionNo = "448548202400918",
                filename = "http://www.animal.go.kr/files/shelter/2024/09/202410231110474_s.jpg",
                happenDt = "20241023",
                happenPlace = "초계면",
                kindCd = "[개] 믹스견",
                colorCd = "갈색",
                age = "2023(년생)",
                weight = "2(Kg)",
                noticeNo = "경남-합천-2024-00433",
                noticeSdt = "20241023",
                noticeEdt = "20241104",
                popfile = "http://www.animal.go.kr/files/shelter/2024/09/202410231110474.jpg",
                processState = "보호중",
                sexCd = "F",
                neuterYn = "N",
                specialMark = "마을배회",
                careNm = "태민동물병원",
                careTel = "010-2868-2108",
                careAddr = "경상남도 합천군 합천읍 옥산로 16 (합천읍, 까치빌라) 태민동물병원",
                orgNm = "경상남도 합천군",
                chargeNm = "고영삼",
                officetel = "055-930-3562"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeatureInfoPreview() {
    WithconimalTheme {
        FeatureInfo(icon = R.drawable.ic_calendar, title = "접수일", text = "20202020")
    }
}

@Preview(showBackground = true)
@Composable
private fun FeatureInfoPreviewDark() {
    WithconimalTheme(darkTheme = true) {
        FeatureInfo(icon = R.drawable.ic_calendar, title = "접수일", text = "20202020")
    }
}