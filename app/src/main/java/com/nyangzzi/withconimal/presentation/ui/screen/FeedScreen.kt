package com.nyangzzi.withconimal.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nyangzzi.withconimal.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.presentation.feature.feed.FeedEvent
import com.nyangzzi.withconimal.presentation.feature.feed.FeedUiState
import com.nyangzzi.withconimal.presentation.feature.feed.FeedViewModel
import com.nyangzzi.withconimal.presentation.navigation.Screens
import com.nyangzzi.withconimal.presentation.util.Utils
import com.nyangzzi.withconimal.ui.theme.WithconimalTheme

@Composable
fun FeedScreen(navController: NavHostController, viewModel: FeedViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = viewModel.animalPagingFlow.collectAsLazyPagingItems()

    FeedContent(
        pagingItems = pagingItems,
        uiState = uiState,
        onClickContent = {
            viewModel.onEvent(FeedEvent.UpdateSelectInfo(data = it))
            navController.navigate(Screens.Detail.route) {
                launchSingleTop = true
                restoreState = true
            }
        })

}

@Composable
private fun FeedContent(
    pagingItems: LazyPagingItems<AnimalInfo>?,
    uiState: FeedUiState, onClickContent: (AnimalInfo) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (uiState.totalCnt > 0) {
            Row(verticalAlignment = Alignment.Bottom) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 32.dp, start = 22.dp, bottom = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = Utils.formatComma(uiState.totalCnt),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = " 마리의 소중한 생명이",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Text(
                        text = "가족을 기다리고 있어요",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(42.dp)
                        .clip(shape = CircleShape)
                        .clickable {  }
                        .padding(8.dp),
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }
        }

        Box {

            val scrollState = rememberLazyListState()
            LazyColumn(
                state = scrollState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                pagingItems?.let {
                    items(pagingItems.itemCount) {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    pagingItems[it]?.let { it1 -> onClickContent(it1) }
                                }
                        ) {
                            AnimalComponent(
                                imageUrl = pagingItems[it]?.popfile ?: "",
                                processState = pagingItems[it]?.processState,
                                kindCd = pagingItems[it]?.kindCd ?: ""
                            )
                        }
                    }
                }

                when {
                    pagingItems?.loadState?.refresh is LoadState.Loading -> {
                        item { CircularProgressIndicator() }
                    }

                    pagingItems?.loadState?.append is LoadState.Loading -> {
                        item { CircularProgressIndicator() }
                    }

                    pagingItems?.loadState?.refresh is LoadState.Error -> {
                        item {
                            Text(text = "Error loading animals")
                        }
                    }
                }

            }

            var isTop by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = isTop) {
                if (isTop) {
                    scrollState.animateScrollToItem(0)
                    isTop = false
                }

            }
            if ((pagingItems?.itemCount ?: 0) > 0) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(30.dp), onClick = { isTop = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_up_line),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

        }
    }
}

@Composable
private fun AnimalComponent(
    imageUrl: String?,
    processState: String?,
    kindCd: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = MaterialTheme.colorScheme.background)
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
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = painterResource(id = R.drawable.ic_loading_image)
                )
            }

            processState?.let {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(color = Color(0xAA000000), shape = RoundedCornerShape(20.dp))
                        .padding(vertical = 6.dp, horizontal = 12.dp)
                ) {
                    Text(text = it, color = Color.White)
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = kindCd,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_heart_line),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_sharing),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.outline
            )

        }


    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    WithconimalTheme {
        FeedContent(uiState = FeedUiState(), pagingItems = null) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreviewDark() {
    WithconimalTheme(darkTheme = true) {
        FeedContent(uiState = FeedUiState(), pagingItems = null) {}
    }
}