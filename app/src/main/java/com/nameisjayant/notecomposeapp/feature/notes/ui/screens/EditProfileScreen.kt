package com.nameisjayant.notecomposeapp.feature.notes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.components.ButtonComponent
import com.nameisjayant.notecomposeapp.components.IconButtonComponent
import com.nameisjayant.notecomposeapp.components.IconComponent
import com.nameisjayant.notecomposeapp.components.SpacerHeight
import com.nameisjayant.notecomposeapp.components.TextFieldComponent
import com.nameisjayant.notecomposeapp.data.model.Auth
import com.nameisjayant.notecomposeapp.data.model.AuthResponse
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteEvent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.utils.ID
import com.nameisjayant.notecomposeapp.utils.ResultState
import com.nameisjayant.notecomposeapp.utils.SOMETHING_WET_WRONG
import com.nameisjayant.notecomposeapp.utils.USER
import com.nameisjayant.notecomposeapp.utils.showMsg
import kotlinx.coroutines.flow.collectLatest


@Composable
fun EditProfileScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val name = navHostController.previousBackStackEntry?.savedStateHandle?.get<String>(USER) ?: ""
    val key = navHostController.previousBackStackEntry?.savedStateHandle?.get<String>(ID) ?: ""
    var username by remember {
        mutableStateOf(name)
    }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        SpacerHeight()
        IconButtonComponent(
            imageVector = Icons.Default.ArrowBack
        ) {
            navHostController.navigateUp()
        }
        IconComponent(
            imageVector = Icons.Default.Person,
            modifier = Modifier
                .size(100.dp)
        )
        SpacerHeight(24.dp)
        TextFieldComponent(
            value = username,
            onValueChange = {
                username = it
            },
            placeholder = stringResource(id = R.string.enter_username),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            maxLine = 1
        )
        SpacerHeight(24.dp)
        ButtonComponent(title = stringResource(R.string.update)) {
            if (username.isNotEmpty())
                viewModel.onEvent(
                    NoteEvent.UpdateUserDetailEvent(
                        AuthResponse(
                            key = key,
                            auth = Auth(username = username)
                        )
                    )
                )
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.updateUserDetailEventFlow.collectLatest {
            isLoading = when (it) {
                is ResultState.Success -> {
                    navHostController.navigateUp()
                    false
                }

                is ResultState.Failure -> {
                    context.showMsg(it.msg.message ?: SOMETHING_WET_WRONG)
                    false
                }

                ResultState.Loading -> true

            }
        }
    }

}