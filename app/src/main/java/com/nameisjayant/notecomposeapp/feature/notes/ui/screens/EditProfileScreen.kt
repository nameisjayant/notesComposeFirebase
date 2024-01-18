package com.nameisjayant.notecomposeapp.feature.notes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.components.ButtonComponent
import com.nameisjayant.notecomposeapp.components.IconButtonComponent
import com.nameisjayant.notecomposeapp.components.IconComponent
import com.nameisjayant.notecomposeapp.components.LoadingComponent
import com.nameisjayant.notecomposeapp.components.SpacerHeight
import com.nameisjayant.notecomposeapp.components.SpacerWidth
import com.nameisjayant.notecomposeapp.components.TextFieldComponent
import com.nameisjayant.notecomposeapp.data.model.Auth
import com.nameisjayant.notecomposeapp.data.model.AuthResponse
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteEvent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.feature.register.ui.screens.GenderRow
import com.nameisjayant.notecomposeapp.feature.register.ui.screens.convertMillisToDate
import com.nameisjayant.notecomposeapp.utils.ID
import com.nameisjayant.notecomposeapp.utils.ResultState
import com.nameisjayant.notecomposeapp.utils.SOMETHING_WET_WRONG
import com.nameisjayant.notecomposeapp.utils.USER
import com.nameisjayant.notecomposeapp.utils.showMsg
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel
) {
    val userProfile by viewModel.profileDetail.collectAsStateWithLifecycle()
    var username by remember {
        mutableStateOf(userProfile?.auth?.username ?: "")
    }
    var phone by remember { mutableStateOf(userProfile?.auth?.mobileNumber ?: "") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var showDateTimerPicker by remember { mutableStateOf(false) }
    val datePickerState =
        rememberDatePickerState(initialDisplayedMonthMillis = userProfile?.auth?.dob)
    var selectedGender by remember { mutableIntStateOf(userProfile?.auth?.gender ?: 0) }
    val genderList = listOf("Male", "Female", "Other")
    val isCompleted by remember {
        derivedStateOf {
            username.trim().isNotEmpty() && phone.trim().isNotEmpty()

        }
    }
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
                imeAction = ImeAction.Next
            ),
            maxLine = 1
        )
        SpacerHeight(24.dp)
        TextFieldComponent(
            value = phone,
            onValueChange = {
                phone = it
            },
            placeholder = stringResource(id = R.string.enter_mobile_number),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            maxLine = 1
        )
      /*  SpacerHeight(24.dp)
        TextFieldComponent(
            value = datePickerState.selectedDateMillis?.let {
                convertMillisToDate(it)
            } ?: "",
            onValueChange = { },
            placeholder = stringResource(R.string.select_date),
            enable = false,
            trailingIcon = {
                IconComponent(imageVector = Icons.Rounded.KeyboardArrowDown)
            }
        ) {
            showDateTimerPicker = true
        }*/
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
        SpacerHeight(24.dp)
        ButtonComponent(title = stringResource(R.string.update)) {
            if (isCompleted)
                viewModel.onEvent(
                    NoteEvent.UpdateUserDetailEvent(
                        AuthResponse(
                            key = userProfile?.key ?: "",
                            auth = Auth(
                                username = username,
                                mobileNumber = phone,
                                dob = datePickerState.selectedDateMillis ?: 0,
                                gender = selectedGender
                            )
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

    if(isLoading) LoadingComponent()
}