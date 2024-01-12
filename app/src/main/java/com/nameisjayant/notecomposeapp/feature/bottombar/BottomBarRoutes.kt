package com.nameisjayant.notecomposeapp.feature.bottombar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person4
import androidx.compose.ui.graphics.vector.ImageVector
import com.nameisjayant.notecomposeapp.R


enum class BottomBarRoutes(
    @StringRes title: Int,
    val route: String,
    val icon: ImageVector
) {
    Note(R.string.notes, "/note", Icons.Default.EditNote),
    Profile(R.string.profile, "/profile", Icons.Default.Person)
}