package com.nameisjayant.notecomposeapp.feature.register.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.components.ButtonComponent
import com.nameisjayant.notecomposeapp.components.IconButtonComponent
import com.nameisjayant.notecomposeapp.components.LoadingComponent
import com.nameisjayant.notecomposeapp.components.SpacerHeight
import com.nameisjayant.notecomposeapp.components.SpacerWidth
import com.nameisjayant.notecomposeapp.components.TextFieldComponent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteEvent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.utils.ResultState
import com.nameisjayant.notecomposeapp.utils.SOMETHING_WET_WRONG
import com.nameisjayant.notecomposeapp.utils.showMsg
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ResetPasswordScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {

    var email by remember { mutableStateOf("") }
    val emailValidationFailedException by viewModel.emailValidation.collectAsStateWithLifecycle()
    val isCompleted by remember {
        derivedStateOf {
            email.trim().isNotEmpty() && emailValidationFailedException.isEmpty()
        }
    }
    val context = LocalContext.current
    var isloading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = email) {
        viewModel.checkEmailValidation(email.trim())
    }

    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Row {
                    IconButtonComponent(imageVector = Icons.Default.ArrowBack) {
                        navHostController.navigateUp()
                    }
                    SpacerWidth()
                    Text(
                        text = stringResource(R.string.reset_password),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.W700
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                SpacerHeight(16.dp)
                Text(text = stringResource(R.string.enter_you_email_below_a_link_will_be_send_to_your_email_address_where_you_can_reset_your_password))
                SpacerHeight(16.dp)
                TextFieldComponent(
                    value = email, onValueChange = { email = it }, placeholder = stringResource(
                        id = R.string.enter_email,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Email
                    ),
                    maxLine = 1
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
            }
        }
        ButtonComponent(title = stringResource(R.string.next), modifier = Modifier.padding(16.dp)) {
            if (isCompleted)
                viewModel.onEvent(
                    NoteEvent.ResetPasswordEvent(
                        email.trim()
                    )
                )
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.resetPasswordEventFlow.collectLatest {
            isloading = when (it) {
                is ResultState.Success -> {
                    context.showMsg(it.data)
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
    if (isloading) LoadingComponent()

}