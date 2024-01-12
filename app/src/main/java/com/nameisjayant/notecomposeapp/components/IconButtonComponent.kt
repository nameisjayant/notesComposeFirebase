package com.nameisjayant.notecomposeapp.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
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
    size: Dp = 24.dp,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = modifier) {
        if (icon != null)
            IconComponent(icon = icon, tint = tint, modifier = Modifier.size(size))
        else
            IconComponent(
                imageVector = imageVector,
                tint = tint,
                modifier = Modifier.size(size)
            )
    }
}

@Composable
fun CustomAppIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    background: Color = Color.Gray,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    IconComponent(
        imageVector = icon, modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .drawBehind {
                drawRoundRect(
                    color = background,
                    cornerRadius = CornerRadius(100f)
                )
            }
            .padding(20.dp)
            .size(30.dp),
        tint = tint
    )
}