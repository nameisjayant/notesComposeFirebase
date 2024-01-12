package com.nameisjayant.notecomposeapp.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.nameisjayant.notecomposeapp.ui.theme.Pink80

@Composable
fun LoadingComponent() {
    Dialog(onDismissRequest = {}) {
        CircularProgressIndicator()
    }
}


@Composable
fun ProgressBarComponent(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(modifier = modifier, color = Pink80)
}