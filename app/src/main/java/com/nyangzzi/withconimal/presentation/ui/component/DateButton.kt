package com.nyangzzi.withconimal.presentation.ui.component

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.ContextThemeWrapper
import android.widget.DatePicker
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.nyangzzi.withconimal.R
import com.nyangzzi.withconimal.domain.model.common.NeuterType
import com.nyangzzi.withconimal.presentation.util.Utils
import com.nyangzzi.withconimal.ui.theme.WithconimalTheme
import java.util.Calendar
import java.util.Date

@Composable
fun DateButton(
    text: String?,
    onClick: (String?) -> Unit,
) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val isDarkTheme =
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }

    val themedContext: Context =
        ContextThemeWrapper(
            context,
            if (isDarkTheme) android.R.style.ThemeOverlay_Material_Dark
            else android.R.style.ThemeOverlay_Material_Light
        )

    // DatePickerDialog 객체
    val datePickerDialog = DatePickerDialog(
        themedContext,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onClick("$year${month + 1}$dayOfMonth")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

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
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(6.dp)
            )
            .clip(RoundedCornerShape(6.dp))
            .clickable { datePickerDialog.show() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (text == null) {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(16.dp),
                painter = painterResource(
                    id = R.drawable.ic_calendar_select
                ), contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            modifier = Modifier
                .padding(start = if (text != null) 16.dp else 0.dp)
                .weight(1f),
            text = Utils.dateFormat(text) ?: "미선택",
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        text?.let {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable { onClick(null) }
                    .padding(6.dp),
                painter = painterResource(
                    id = R.drawable.ic_close
                ), contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    WithconimalTheme {
        DateButton(
            text = "전체",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    WithconimalTheme(darkTheme = true) {
        DateButton(
            text = "전체",
            onClick = {}
        )
    }
}