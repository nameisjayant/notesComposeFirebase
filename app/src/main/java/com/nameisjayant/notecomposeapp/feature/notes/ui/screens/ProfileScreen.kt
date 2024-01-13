package com.nameisjayant.notecomposeapp.feature.notes.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.components.ButtonComponent
import com.nameisjayant.notecomposeapp.components.CustomAppIcon
import com.nameisjayant.notecomposeapp.components.IconComponent
import com.nameisjayant.notecomposeapp.components.ProgressBarComponent
import com.nameisjayant.notecomposeapp.components.SpacerHeight
import com.nameisjayant.notecomposeapp.components.TextFieldComponent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteEvent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.feature.navigation.NavigationRoutes
import com.nameisjayant.notecomposeapp.utils.ID
import com.nameisjayant.notecomposeapp.utils.USER
import com.nameisjayant.notecomposeapp.utils.navigateToWithPopping


@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val userResponse by viewModel.getUserDetailEventFlow.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()

    ) {
        CustomAppIcon(
            icon = Icons.Default.ModeEdit,
            tint = Color.White,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            navHostController.currentBackStackEntry?.savedStateHandle?.set(
                USER,
                userResponse.data[0].auth?.username ?: ""
            )
            navHostController.currentBackStackEntry?.savedStateHandle?.set(
                ID,
                userResponse.data[0].key ?: ""
            )
            navHostController.navigate(NavigationRoutes.EditProfile.route)
        }
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            if (userResponse.data.isNotEmpty()) {
                IconComponent(
                    imageVector = Icons.Default.Person,
                    modifier = Modifier
                        .size(100.dp)
                )
                SpacerHeight(30.dp)
                TextFieldComponent(
                    value = userResponse.data[0].auth?.username ?: "",
                    onValueChange = {},
                    placeholder = "",
                    enable = false
                )
                SpacerHeight(30.dp)
                TextFieldComponent(
                    value = userResponse.data[0].auth?.email ?: "",
                    onValueChange = {},
                    placeholder = "",
                    enable = false
                )
                SpacerHeight(30.dp)
            }
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
        if (userResponse.isLoading)
            ProgressBarComponent(modifier = Modifier.align(Alignment.Center))
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(NoteEvent.GetUserDetailEvent)
    }
    BackHandler {
        navigateToWithPopping(navHostController,NavigationRoutes.Main.route)
    }
}