package com.nyangzzi.withconimal.presentation.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nyangzzi.withconimal.R
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.presentation.feature.feed.FeedViewModel
import com.nyangzzi.withconimal.presentation.util.Utils
import com.nyangzzi.withconimal.ui.theme.WithconimalTheme

@Composable
fun DetailScreen(info: AnimalInfo) {

    DetailContent(info = info)

}

@Composable
fun DetailContent(info: AnimalInfo) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {

            Text(
                "나를 소개할게요!",
                fontSize = 24.sp,
                fontWeight = FontWeight(800),
                color = MaterialTheme.colorScheme.onSurface
            )

            AnimalImage(imageUrl = info.popfile)

            DetailInfo(
                specialMark = info.specialMark,
                kindCd = info.kindCd,
                colorCd = info.colorCd,
                age = info.age,
                weight = info.weight,
                sexCd = info.sexCd,
                neuterYn = info.neuterYn
            )

        }
    }
}

@Composable
private fun AnimalImage(imageUrl: String?) {

    imageUrl?.let {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Utils.setImageUrl(imageUrl))
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            placeholder = painterResource(id = R.drawable.ic_loading_image)
        )
    }
}

@Composable
private fun DetailInfo(
    specialMark: String?,
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
            icon = when (neuterYn) {
                "Y" -> R.drawable.ic_neuter_yes
                "N" -> R.drawable.ic_neuter_no
                else -> R.drawable.ic_neuter_dont
            },
            title = "중성화",
            text = when (neuterYn) {
                "Y" -> "완료"
                "N" -> "미완료"
                else -> "확인불가"
            }
        )
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
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
    }

    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = StaggeredGridCells.Adaptive(100.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(infoList.size) {
            FeatureInfo(
                icon = infoList[it].icon,
                title = infoList[it].title,
                text = infoList[it].text
            )
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
                .defaultMinSize(minWidth = 84.dp)
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

@Preview(showBackground = true)
@Composable
private fun Preview() {
    WithconimalTheme {
        DetailContent(
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