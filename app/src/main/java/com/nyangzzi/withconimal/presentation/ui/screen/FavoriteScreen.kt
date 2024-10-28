package com.nyangzzi.withconimal.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nyangzzi.withconimal.R
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.presentation.feature.feed.FeedEvent
import com.nyangzzi.withconimal.presentation.feature.feed.FeedViewModel
import com.nyangzzi.withconimal.presentation.navigation.Screens
import com.nyangzzi.withconimal.presentation.util.Utils
import com.nyangzzi.withconimal.ui.theme.WithconimalTheme

@Composable
fun FavoriteScreen(
    navController: NavHostController,
    viewModel: FeedViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoriteAnimal by viewModel.favoriteAnimal.collectAsStateWithLifecycle()

    if (favoriteAnimal.isEmpty()) {
        FavoriteEmptyList()
    } else {
        FavoriteContent(
            favoriteAnimal = favoriteAnimal,
            onClickContent = {
                viewModel.onEvent(FeedEvent.UpdateSelectInfo(data = it))
                navController.navigate(Screens.Detail.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }, onEvent = viewModel::onEvent
        )
    }

}

@Composable
private fun FavoriteEmptyList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_smile_square),
            modifier = Modifier.size(56.dp),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "아직 반려 후보가 없어요",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "조금 더 찾아 볼까요?",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}

@Composable
private fun FavoriteContent(
    favoriteAnimal: List<AnimalInfo>,
    onClickContent: (AnimalInfo) -> Unit,
    onEvent: (FeedEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 32.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = Utils.formatComma(favoriteAnimal.size),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = " 번의 소중한 마음을",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                text = "하나로 모아 뒀어요",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        FavoriteAnimalList(
            favoriteAnimal = favoriteAnimal,
            onClickContent = onClickContent,
            onEvent = onEvent
        )
    }
}

@Composable
private fun FavoriteAnimalList(
    favoriteAnimal: List<AnimalInfo>,
    onClickContent: (AnimalInfo) -> Unit,
    onEvent: (FeedEvent) -> Unit
) {
    val scrollState = rememberLazyGridState()
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        state = scrollState,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        items(favoriteAnimal.size) { item ->
            Box(
                modifier = Modifier
                    .padding(bottom = if (item == favoriteAnimal.lastIndex) 12.dp else 0.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .clickable {
                        onClickContent(favoriteAnimal[item])
                    }
            ) {
                val isFavorite =
                    favoriteAnimal.any { it.desertionNo == favoriteAnimal[item].desertionNo }
                AnimalComponent(
                    isFavorite = isFavorite,
                    imageUrl = favoriteAnimal[item].popfile ?: "",
                    processState = favoriteAnimal[item].processState,
                    kindCd = favoriteAnimal[item].kindCd?.replace("[개]", "[강아지]") ?: "",
                    onClickFavorite = {
                        if (isFavorite) onEvent(
                            FeedEvent.DeleteFavoriteAnimal(
                                favoriteAnimal[item]
                            )
                        )
                        else onEvent(
                            FeedEvent.AddFavoriteAnimal(
                                favoriteAnimal[item]
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AnimalComponent(
    isFavorite: Boolean,
    imageUrl: String?,
    processState: String?,
    kindCd: String,
    onClickFavorite: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Box {
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
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = painterResource(id = R.drawable.ic_loading_image)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(22.dp),
                painter = painterResource(
                    id = if (kindCd.contains("[개]") || kindCd.contains("[강아지]")) R.drawable.ic_kind_dog
                    else if (kindCd.contains("[고양이]")) R.drawable.ic_kind_cat
                    else R.drawable.ic_kind_etc,
                ),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                text = kindCd.replace("[개] ", "")
                    .replace("[강아지] ", "")
                    .replace("[고양이] ", "")
                    .replace("[기타축종] ", ""),
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground
            )

            processState?.let {
                Text(text = it, color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp)
            }


        }


    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {
    WithconimalTheme {
        FavoriteContent(favoriteAnimal = emptyList(), onClickContent = {}, onEvent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreviewDark() {
    WithconimalTheme(darkTheme = true) {
        FavoriteContent(favoriteAnimal = emptyList(), onClickContent = {}, onEvent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    WithconimalTheme {
        FavoriteEmptyList()
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreviewDark() {
    WithconimalTheme(darkTheme = true) {
        FavoriteEmptyList()
    }
}