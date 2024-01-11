package com.nameisjayant.notecomposeapp.components


import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource


@Composable
fun IconDrawableComponent(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    imageVector: ImageVector? = null,
    tint: Color = Color.Unspecified
) {
    if (icon != null)
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
    else
        Icon(
            imageVector = imageVector!!,
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
}