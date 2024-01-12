package com.nameisjayant.notecomposeapp.feature.notes.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.components.HorizontalDivider
import com.nameisjayant.notecomposeapp.components.IconComponent
import com.nameisjayant.notecomposeapp.components.NoteEachRow
import com.nameisjayant.notecomposeapp.components.ProgressBarComponent
import com.nameisjayant.notecomposeapp.components.SearchFieldComponent
import com.nameisjayant.notecomposeapp.components.SpacerHeight
import com.nameisjayant.notecomposeapp.data.model.Note
import com.nameisjayant.notecomposeapp.data.model.NoteResponse
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteEvent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.feature.navigation.NavigationRoutes
import com.nameisjayant.notecomposeapp.ui.theme.Pink80
import com.nameisjayant.notecomposeapp.ui.theme.Purple80


@Composable
fun NoteScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel
) {
    var search by remember { mutableStateOf("") }
    val noteResponse by viewModel.showNotesEventFlow.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                SpacerHeight(16.dp)
                Text(text = "Hi, Jayant")
                SpacerHeight(16.dp)
                SearchFieldComponent(value = search, onValueChange = { search = it })
            }
            if (noteResponse.data.isNotEmpty())
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(noteResponse.data, key = { it.id ?: it.hashCode() }) {
                        NoteEachRow(note = NoteResponse(
                            "",
                            it.note
                        ), editNote = { }, deleteNote = {})
                    }
                }
        }
        FloatingActionButton(
            onClick = {
                navHostController.navigate(NavigationRoutes.AddEditNote.route)
            },
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomEnd),
            containerColor = Pink80
        ) {
            IconComponent(imageVector = Icons.Default.Add)
        }

        if (noteResponse.isLoading)
            ProgressBarComponent(
                modifier = Modifier.align(Alignment.Center)
            )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(NoteEvent.GetNoteEvent)
    }

}