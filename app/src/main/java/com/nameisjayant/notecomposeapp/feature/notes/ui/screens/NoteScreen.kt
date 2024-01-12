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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.components.IconComponent
import com.nameisjayant.notecomposeapp.components.NoteEachRow
import com.nameisjayant.notecomposeapp.components.ProgressBarComponent
import com.nameisjayant.notecomposeapp.components.SearchFieldComponent
import com.nameisjayant.notecomposeapp.components.SpacerHeight
import com.nameisjayant.notecomposeapp.data.model.NoteResponse
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteEvent
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.feature.navigation.NavigationRoutes
import com.nameisjayant.notecomposeapp.ui.theme.Pink80
import com.nameisjayant.notecomposeapp.utils.ResultState
import com.nameisjayant.notecomposeapp.utils.SOMETHING_WET_WRONG
import com.nameisjayant.notecomposeapp.utils.showMsg
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale


@Composable
fun NoteScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel
) {
    var search by remember { mutableStateOf("") }
    val noteResponse by viewModel.showNotesEventFlow.collectAsStateWithLifecycle()
    val userDetail by viewModel.getUserDetailEventFlow.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                SpacerHeight(16.dp)
                if (userDetail.data.isNotEmpty())
                    Text(
                        text = "Hi, ${userDetail.data[0].auth?.username}\uD83D\uDC4B",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.W700
                        )
                    )
                SpacerHeight(16.dp)
                SearchFieldComponent(value = search, onValueChange = { search = it })
            }
            if (noteResponse.data.isNotEmpty())
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        searchNotes(search, noteResponse.data),
                        key = { it.id ?: it.hashCode() }) {
                        NoteEachRow(note = NoteResponse(
                            "",
                            it.note
                        ), editNote = {
                            viewModel.setNote(it)
                            navHostController.navigate(NavigationRoutes.AddEditNote.route)
                        }, deleteNote = {
                            viewModel.onEvent(NoteEvent.DeleteNoteEvent(it))
                        })
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
        viewModel.onEvent(NoteEvent.GetUserDetailEvent)
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.deleteNoteEventFlow.collectLatest {
            isLoading = when (it) {
                is ResultState.Success -> false
                is ResultState.Failure -> {
                    context.showMsg(it.msg.message ?: SOMETHING_WET_WRONG)
                    false
                }

                ResultState.Loading -> true

            }
        }
    }

}

private fun searchNotes(
    search: String,
    data: List<NoteResponse>
): List<NoteResponse> {
    return if (search.isEmpty())
        data
    else {
        val res = ArrayList<NoteResponse>()
        for (temp in data) {
            if (temp.note?.title?.lowercase(Locale.getDefault())
                    ?.contains(search.lowercase(Locale.getDefault())) == true ||
                temp.note?.description?.lowercase(Locale.getDefault())
                    ?.contains(search.lowercase(Locale.getDefault())) == true
            )
                res.add(temp)
        }
        res
    }
}