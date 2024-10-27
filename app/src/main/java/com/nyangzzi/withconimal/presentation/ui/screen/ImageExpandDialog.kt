package com.nyangzzi.withconimal.presentation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nyangzzi.withconimal.R
import com.nyangzzi.withconimal.presentation.util.Utils

@Composable
fun ImageExpandDialog(isShown: Boolean,
                      imageUrl: String?,
                      onDismiss:() -> Unit
) {

    if (isShown && imageUrl != null) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            ImageContent(imageUrl = imageUrl, onDismiss = onDismiss)
        }
    }


}

@Composable
private fun ImageContent(imageUrl: String,onDismiss:() -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var screenWidth by remember { mutableStateOf(0) }
    var screenHeight by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                // 디바이스 화면 크기를 가져옴
                screenWidth = coordinates.size.width
                screenHeight = coordinates.size.height
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    // 업데이트된 스케일 제한
                    val newScale = (scale * zoom).coerceAtLeast(1f)
                    val scaledWidth = imageSize.width * newScale
                    val scaledHeight = imageSize.height * newScale

                    // 확대 상태에서만 이동 제한을 적용
                    val maxXOffset = ((scaledWidth - screenWidth) / 2).coerceAtLeast(0f)
                    val maxYOffset = ((scaledHeight - screenHeight) / 2).coerceAtLeast(0f)

                    // 확대 비율에 따른 이동 계산
                    val newOffsetX = (offsetX + pan.x).coerceIn(-maxXOffset, maxXOffset)
                    val newOffsetY = (offsetY + pan.y).coerceIn(-maxYOffset, maxYOffset)

                    scale = newScale
                    offsetX = newOffsetX
                    offsetY = newOffsetY
                }
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Utils.setImageUrl(imageUrl))
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    imageSize = coordinates.size
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
                .fillMaxSize()
        )

        Icon(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .size(56.dp)
                .clip(shape = CircleShape)
                .clickable { onDismiss() }
                .padding(12.dp),
            tint = Color.White,
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = ""
        )
    }
}