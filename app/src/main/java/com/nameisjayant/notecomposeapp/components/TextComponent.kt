package com.nameisjayant.notecomposeapp.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight


@Composable
fun RegisterLoginTextComponent(
    text: String
) {
    Text(
        text = text, style = MaterialTheme.typography.headlineLarge.copy(
            color = Color.DarkGray,
            fontWeight = FontWeight.W600

        )
    )
}