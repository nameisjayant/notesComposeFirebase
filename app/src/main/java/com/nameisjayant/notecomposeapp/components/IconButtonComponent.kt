package com.nameisjayant.notecomposeapp.components
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconButtonComponent(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    imageVector: ImageVector? = null,
    tint: Color = Color.Unspecified,
    size: Dp = 24.dp
) {
    IconButton(onClick = {}, modifier = modifier) {
        if (icon != null)
            IconDrawableComponent(icon = icon, tint = tint, modifier = Modifier.size(size))
        else
            IconDrawableComponent(
                imageVector = imageVector,
                tint = tint,
                modifier = Modifier.size(size)
            )
    }
}