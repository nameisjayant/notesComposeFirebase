package com.nameisjayant.notecomposeapp.feature.register.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.components.ButtonComponent
import com.nameisjayant.notecomposeapp.components.HavAccountComponent
import com.nameisjayant.notecomposeapp.components.IconButtonComponent
import com.nameisjayant.notecomposeapp.components.IconComponent
import com.nameisjayant.notecomposeapp.components.LoadingComponent
import com.nameisjayant.notecomposeapp.components.RegisterLoginTextComponent
import com.nameisjayant.notecomposeapp.components.SpacerHeight
import com.nameisjayant.notecomposeapp.components.SpacerWidth
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current.getActivity()!!
    val emailValidationFailedException by viewModel.emailValidation.collectAsStateWithLifecycle()
    val passwordValidation by viewModel.passwordValidation.collectAsStateWithLifecycle()

    var showDateTimerPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val date by remember {
        derivedStateOf {
            datePickerState.selectedDateMillis?.let {
                convertMillisToDate(it)
            }
        }
    }
    var selectedGender by remember { mutableIntStateOf(0) }
    val genderList = listOf("Male", "Female", "Other")
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var phone by remember { mutableStateOf("") }
    val userId by viewModel.getPref(PreferenceStore.userId)
        .collectAsStateWithLifecycle(initialValue = "")
    val isCompleted by remember {
        derivedStateOf {
            email.isNotEmpty() && password.isNotEmpty() && emailValidationFailedException.isEmpty()
                    && passwordValidation.isEmpty() && date?.trim()?.isNotEmpty() == true && phone.trim().isNotEmpty()
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
                RegisterLoginTextComponent(text = stringResource(R.string.register))
                SpacerHeight(24.dp)
                TextFieldComponent(
                    value = name, onValueChange = { name = it }, placeholder = stringResource(
                        R.string.enter_username
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    maxLine = 1
                )
                SpacerHeight(16.dp)
                TextFieldComponent(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = stringResource(R.string.enter_email),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
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
                SpacerHeight(16.dp)
                TextFieldComponent(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = stringResource(R.string.enter_password),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    trailingIcon = {
                        IconButtonComponent(
                            imageVector = Icons.Default.RemoveRedEye,
                            tint = if (isPasswordVisible) Color.Black else Color.LightGray
                        ) {
                            isPasswordVisible = !isPasswordVisible
                        }
                    },
                    isPasswordVisible = isPasswordVisible,
                    maxLine = 1
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
                SpacerHeight(16.dp)
                Text(
                    text = stringResource(R.string.select_gender),
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier.fillMaxWidth()

                )
                SpacerHeight(24.dp)
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    genderList.forEachIndexed { index, s ->
                        GenderRow(
                            title = s,
                            selected = selectedGender == index,
                            index = index,
                            onValueUpdate = {
                                selectedGender = it
                            })
                    }
                }
                SpacerHeight(16.dp)
                TextFieldComponent(
                    value =  datePickerState.selectedDateMillis?.let {
                        convertMillisToDate(it)
                    } ?: "",
                    onValueChange = { },
                    placeholder = stringResource(R.string.select_date),
                    modifier = Modifier.weight(0.5f),
                    enable = false,
                    trailingIcon = {
                        IconComponent(imageVector = Icons.Rounded.KeyboardArrowDown)
                    }
                ) {
                    showDateTimerPicker = true
                }
                SpacerHeight(16.dp)
                TextFieldComponent(
                    value = phone,
                    onValueChange = {
                        if (it.isDigitsOnly())
                            phone = it
                    },
                    placeholder = stringResource(id = R.string.enter_mobile_number),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Phone
                    )
                )
            }
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ButtonComponent(
                title = stringResource(id = R.string.register),
            ) {
                if (isCompleted)
                    viewModel.onEvent(
                        NoteEvent.RegisterUserEvent(
                            Auth(
                                username = name,
                                email, password
                            )
                        )
                    )
            }
            SpacerHeight()
            HavAccountComponent(
                text1 = stringResource(id = R.string.already_have_an_account),
                text2 = stringResource(id = R.string.login),
                onClick = {
                    navigateToWithPopping(navHostController, NavigationRoutes.Login.route)
                }
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.addUserDetailEventFlow.collectLatest {
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



    LaunchedEffect(key1 = Unit) {
        viewModel.createUserEventFlow.collectLatest {
            isLoading = when (it) {
                is ResultState.Success -> {
                    delay(3000)
                    viewModel.onEvent(
                        NoteEvent.AddUserDetailEvent(
                            Auth(
                                username = name,
                                email, password,
                                gender = selectedGender,
                                dob =  datePickerState.selectedDateMillis ?: 0,
                                mobileNumber = phone
                            ),
                            id = userId
                        )
                    )
                    true
                }

                is ResultState.Failure -> {
                    context.showMsg(it.msg.message ?: SOMETHING_WET_WRONG)
                    false
                }

                ResultState.Loading -> true

            }
        }
    }

    if (isLoading) LoadingComponent()

    BackHandler {
        context.finish()
    }

    if (showDateTimerPicker) {
        DatePickerDialog(onDismissRequest = { }, confirmButton = {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                ButtonComponent(
                    title = stringResource(R.string.confirm),
                    modifier = Modifier.weight(0.5f)
                ) {
                    showDateTimerPicker = false
                }
                SpacerWidth()
                ButtonComponent(
                    title = stringResource(R.string.cancel),
                    background = Color.LightGray,
                    color = Color.Gray,
                    modifier = Modifier.weight(0.5f)
                ) {
                    showDateTimerPicker = false
                }
            }
        }, colors = DatePickerDefaults.colors(
            selectedDayContainerColor = Color.Black,

            )
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun GenderRow(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    index: Int,
    onValueUpdate: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .padding(end = 10.dp)
            .toggleable(
                value = selected,
                onValueChange = {
                    onValueUpdate(index)
                },
                role = Role.RadioButton,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(text = title, color = Color.Black)
    }
}

@SuppressLint("SimpleDateFormat")
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}