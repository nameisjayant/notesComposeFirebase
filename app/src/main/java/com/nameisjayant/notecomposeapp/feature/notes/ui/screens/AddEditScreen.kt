package com.nameisjayant.notecomposeapp.feature.notes.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.components.IconComponent
import com.nameisjayant.notecomposeapp.components.LoadingComponent
import com.nameisjayant.notecomposeapp.components.TextFieldComponent
import com.nameisjayant.notecomposeapp.data.model.Note
import com.nameisjayant.notecomposeapp.data.model.NoteResponse
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteEvent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.utils.ResultState
import com.nameisjayant.notecomposeapp.utils.SOMETHING_WET_WRONG
import com.nameisjayant.notecomposeapp.utils.showMsg
import kotlinx.coroutines.flow.collectLatest


@Composable
fun AddEditNoteScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel
) {
    val currentNote by viewModel.editNote.collectAsStateWithLifecycle()
    var title by remember {
        mutableStateOf(
            if (currentNote == null) "" else currentNote?.note?.title ?: ""

        )
    }
    var description by remember {
        mutableStateOf(
            if (currentNote == null) "" else currentNote?.note?.description ?: ""

        )
    }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    navHostController.navigateUp()
                    viewModel.setNote(null)
                }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Text(
                    text = if (currentNote == null) stringResource(R.string.add_note) else stringResource(
                        R.string.edit_note
                    ), style = TextStyle(
                        color = Color.Black, fontWeight = FontWeight.W600, fontSize = 20.sp
                    )
                )
                if (title.isNotEmpty() && description.isNotEmpty())
                    IconButton(onClick = {
                        if (currentNote == null)
                            viewModel.onEvent(
                                NoteEvent.AddNoteEvent(
                                    Note(
                                        title = title, description = description
                                    )
                                )
                            )
                        else viewModel.onEvent(
                            NoteEvent.UpdateNoteEvent(
                                NoteResponse(
                                    id = currentNote?.id ?: "",
                                    note = Note(
                                        title = title, description = description,
                                    )
                                )
                            )
                        )
                    }, modifier = Modifier.size(24.dp)) {
                        IconComponent(imageVector = Icons.Default.Check, tint = Color.Black)
                    }
                else Box {}
            }
            Spacer(modifier = Modifier.height(20.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(), color = Color.LightGray.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextFieldComponent(
                value = title, placeholder = stringResource(id = R.string.title), style = TextStyle(
                    color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.W600
                ), onValueChange = {
                    title = it
                }, maxLine = 1, keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            TextFieldComponent(
                value = description,
                placeholder = stringResource(id = R.string.add_note_),
                style = TextStyle(
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                ),
                onValueChange = {
                    description = it
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    BackHandler {
        navHostController.navigateUp()
        viewModel.setNote(null)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.updateNoteEventFlow.collectLatest {
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

    LaunchedEffect(key1 = Unit) {
        viewModel.addNoteEventFlow.collectLatest {
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

    if (isLoading) LoadingComponent()

}