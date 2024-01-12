package com.nameisjayant.notecomposeapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.LightGray.copy(alpha = 0.6f)
) {
    Divider(modifier = modifier
        .height(1.dp)
        .fillMaxWidth(), color = color)
}