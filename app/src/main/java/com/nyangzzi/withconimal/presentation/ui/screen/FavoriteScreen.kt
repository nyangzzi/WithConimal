package com.nyangzzi.withconimal.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nyangzzi.withconimal.R
import com.nyangzzi.withconimal.presentation.feature.feed.FeedViewModel
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
        FavoriteContent()
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
private fun FavoriteContent() {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .padding(top = 32.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = Utils.formatComma(0),
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
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {
    WithconimalTheme {
        FavoriteContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreviewDark() {
    WithconimalTheme(darkTheme = true) {
        FavoriteContent()
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