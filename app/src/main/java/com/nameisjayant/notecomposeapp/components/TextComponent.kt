package com.nameisjayant.notecomposeapp.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun RegisterLoginTextComponent(
    text: String
) {
    Text(
        text = text, style = MaterialTheme.typography.headlineMedium.copy(
            color = Color.Black,
        )
    )
}