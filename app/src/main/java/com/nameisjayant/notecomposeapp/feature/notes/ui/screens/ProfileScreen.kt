package com.nameisjayant.notecomposeapp.feature.notes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.components.ButtonComponent
import com.nameisjayant.notecomposeapp.components.CustomAppIcon
import com.nameisjayant.notecomposeapp.components.IconComponent
import com.nameisjayant.notecomposeapp.components.SpacerHeight
import com.nameisjayant.notecomposeapp.components.TextFieldComponent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteEvent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.feature.navigation.NavigationRoutes
import com.nameisjayant.notecomposeapp.utils.navigateToWithPopping


@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconComponent(
                    imageVector = Icons.Default.Person,
                    modifier = Modifier
                        .size(100.dp)
                )
                CustomAppIcon(
                    icon = Icons.Default.ModeEdit,
                    tint = Color.White
                ) {

                }
            }
            SpacerHeight(30.dp)
            TextFieldComponent(
                value = "Jayant",
                onValueChange = {},
                placeholder = "",
                enable = false
            )
            SpacerHeight(30.dp)
            TextFieldComponent(
                value = "nameisjayant@gmail.com",
                onValueChange = {},
                placeholder = "",
                enable = false
            )
            SpacerHeight(30.dp)
            ButtonComponent(
                title = stringResource(R.string.logout),
                color = Color.White,
                background = Color.Red.copy(0.6f)
            ) {
                viewModel.onEvent(NoteEvent.SignOutEvent)
                viewModel.clearPrefs()
                navigateToWithPopping(
                    navHostController,
                    NavigationRoutes.Login.route
                )
            }
        }

    }
}