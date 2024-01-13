package com.nameisjayant.notecomposeapp.feature.register.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.components.ButtonComponent
import com.nameisjayant.notecomposeapp.components.HavAccountComponent
import com.nameisjayant.notecomposeapp.components.LoadingComponent
import com.nameisjayant.notecomposeapp.components.RegisterLoginTextComponent
import com.nameisjayant.notecomposeapp.components.SpacerHeight
import com.nameisjayant.notecomposeapp.components.TextFieldComponent
import com.nameisjayant.notecomposeapp.data.model.Auth
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteEvent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.feature.navigation.NavigationRoutes
import com.nameisjayant.notecomposeapp.utils.ONE
import com.nameisjayant.notecomposeapp.utils.PreferenceStore
import com.nameisjayant.notecomposeapp.utils.ResultState
import com.nameisjayant.notecomposeapp.utils.SOMETHING_WET_WRONG
import com.nameisjayant.notecomposeapp.utils.getActivity
import com.nameisjayant.notecomposeapp.utils.navigateToWithPopping
import com.nameisjayant.notecomposeapp.utils.showMsg
import kotlinx.coroutines.flow.collectLatest


@Composable
fun LoginScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current.getActivity()!!
    val emailValidationFailedException by viewModel.emailValidation.collectAsStateWithLifecycle()
    val passwordValidation by viewModel.passwordValidation.collectAsStateWithLifecycle()
    val isCompleted by remember {
        derivedStateOf {
            email.isNotEmpty() && password.isNotEmpty() && emailValidationFailedException.isEmpty()
                    && passwordValidation.isEmpty()
        }
    }


    LaunchedEffect(key1 = email) {
        viewModel.checkEmailValidation(email.trim())
    }
    LaunchedEffect(key1 = password) {
        viewModel.checkPasswordValidation(password.trim())
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            item {
                SpacerHeight()
                RegisterLoginTextComponent(text = stringResource(R.string.login))
                SpacerHeight(24.dp)
                TextFieldComponent(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = stringResource(R.string.enter_email),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    )
                )
                if (emailValidationFailedException.isNotEmpty() && email.isNotEmpty()) {
                    SpacerHeight()
                    Text(
                        text = emailValidationFailedException,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SpacerHeight(16.dp)
                TextFieldComponent(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = stringResource(R.string.enter_password),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    )
                )
                if (passwordValidation.isNotEmpty() && password.isNotEmpty()) {
                    SpacerHeight()
                    Text(
                        text = passwordValidation,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SpacerHeight()
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    ClickableText(
                        text = AnnotatedString(stringResource(R.string.forget_password)),
                        onClick = {
                            navHostController.navigate(NavigationRoutes.ResetPassword.route)
                        },
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ButtonComponent(
                title = stringResource(id = R.string.login),
            ) {
                if (isCompleted)
                    viewModel.onEvent(
                        NoteEvent.LoginUserEvent(
                            Auth(
                                email = email,
                                password = password
                            )
                        )
                    )
            }
            SpacerHeight()
            HavAccountComponent(
                text1 = stringResource(R.string.don_t_have_an_account),
                text2 = stringResource(id = R.string.register),
                onClick = {
                    navigateToWithPopping(navHostController, NavigationRoutes.Register.route)
                }
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loginUserEventFlow.collectLatest {
            isLoading = when (it) {
                is ResultState.Success -> {
                    viewModel.setPref(PreferenceStore.index, ONE)
                    navHostController.navigate(NavigationRoutes.Main.route)
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

    BackHandler {
        context.finish()
    }

    if (isLoading)
        LoadingComponent()
}