package com.nyangzzi.withconimal.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.nyangzzi.withconimal.R
import com.nyangzzi.withconimal.domain.model.common.NeuterType
import com.nyangzzi.withconimal.ui.theme.WithconimalTheme

@Composable
fun DropDown(
    text: String,
    isExpanded: Boolean,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        DropDownButton(
            text = text,
            isEnabled = isEnabled,
            isExpanded = isExpanded,
            onClick = onClick
        )

        Spacer(modifier = Modifier.height(6.dp))

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onClick
        ) {
            content()
        }
    }
}

@Composable
inline fun DropDownItem(
    crossinline onItemClick: () -> Unit,
    crossinline onDismiss: () -> Unit,
    text: String
) {
    DropdownMenuItem(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        text = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        onClick = {
            onItemClick()
            onDismiss()
        },
        colors = MenuDefaults.itemColors(
            textColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun DropDownButton(
    text: String,
    isEnabled: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {

    val modifier = if (isEnabled) Modifier
        .clickable { onClick() }
        .padding(start = 16.dp, end = 12.dp) else Modifier.padding(start = 16.dp, end = 12.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(6.dp)
            )
            .background(
                color = if (isEnabled) MaterialTheme.colorScheme.surfaceContainer
                else MaterialTheme.colorScheme.inverseOnSurface,
                shape = RoundedCornerShape(6.dp)
            )
            .clip(RoundedCornerShape(6.dp))
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            modifier = Modifier.weight(1f),
            text = text,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            color = if (isEnabled) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.inverseSurface.copy(0.5f)
        )

        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(
                id =
                if (isExpanded) R.drawable.ic_up_line else R.drawable.ic_down_line
            ), contentDescription = "",
            tint = if (isEnabled) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.inverseSurface.copy(0.5f)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    WithconimalTheme {
        DropDown(
            text = "전체",
            isExpanded = true,
            onClick = {}
        ) {}
    }
}

@Preview
@Composable
private fun PreviewDark() {
    WithconimalTheme(darkTheme = true) {
        DropDown(
            text = "전체",
            isExpanded = true,
            onClick = {}
        ) {}
    }
}