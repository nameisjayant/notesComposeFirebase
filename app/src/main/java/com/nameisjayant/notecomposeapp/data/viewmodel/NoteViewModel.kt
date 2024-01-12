package com.nameisjayant.notecomposeapp.data.viewmodel

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nameisjayant.notecomposeapp.data.model.Auth
import com.nameisjayant.notecomposeapp.data.model.Note
import com.nameisjayant.notecomposeapp.data.model.NoteResponse
import com.nameisjayant.notecomposeapp.data.repository.NoteRepository
import com.nameisjayant.notecomposeapp.utils.EMAIL_PATTERN
import com.nameisjayant.notecomposeapp.utils.INVALID_EMAIL
import com.nameisjayant.notecomposeapp.utils.PreferenceStore
import com.nameisjayant.notecomposeapp.utils.ResultState
import com.nameisjayant.notecomposeapp.utils.SOMETHING_WET_WRONG
import com.nameisjayant.notecomposeapp.utils.doOnFailure
import com.nameisjayant.notecomposeapp.utils.doOnLoading
import com.nameisjayant.notecomposeapp.utils.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(

    private val noteRepository: NoteRepository,
    private val preferenceStore: PreferenceStore
) : ViewModel() {

    private val _emailValidation: MutableStateFlow<String> = MutableStateFlow("")
    var emailValidation = _emailValidation.asStateFlow()
        private set

    private val _createUserEventFlow: MutableSharedFlow<ResultState<String>> = MutableSharedFlow()
    var createUserEventFlow = _createUserEventFlow.asSharedFlow()
        private set

    private val _addUserDetailEventFlow: MutableSharedFlow<ResultState<String>> =
        MutableSharedFlow()
    var addUserDetailEventFlow = _addUserDetailEventFlow.asSharedFlow()
        private set

    private val _loginUserEventFlow: MutableSharedFlow<ResultState<String>> =
        MutableSharedFlow()
    var loginUserEventFlow = _loginUserEventFlow.asSharedFlow()
        private set

    private val _addNoteEventFlow: MutableSharedFlow<ResultState<String>> =
        MutableSharedFlow()
    var addNoteEventFlow = _addNoteEventFlow.asSharedFlow()
        private set
    private val _editNote: MutableStateFlow<NoteResponse?> = MutableStateFlow(null)
    var editNote = _editNote.asStateFlow()
        private set
    private val _showNotesEventFlow: MutableStateFlow<ComposeUi<NoteResponse>> = MutableStateFlow(
        ComposeUi()
    )
    var showNotesEventFlow = _showNotesEventFlow.asStateFlow()
        private set

    fun setNote(notes: NoteResponse?) {
        _editNote.value = notes
    }

    fun checkEmailValidation(email: String) {
        if (email.isEmpty())
            _emailValidation.value = ""
        else
            if (!Pattern.compile(
                    EMAIL_PATTERN
                ).matcher(email).matches()
            ) {
                _emailValidation.value = INVALID_EMAIL
            } else _emailValidation.value = ""
    }

    fun setPref(key: Preferences.Key<String>, value: String) = viewModelScope.launch {
        preferenceStore.setPref(key, value)
    }

    fun getPref(key: Preferences.Key<String>) = preferenceStore.getPref(key)
    fun clearPrefs() = viewModelScope.launch { preferenceStore.clearDataStore() }

    fun onEvent(event: NoteEvent) = viewModelScope.launch {
        when (event) {
            is NoteEvent.AddNoteEvent -> {
                noteRepository.addNote(event.note)
                    .doOnLoading {
                        _addNoteEventFlow.emit(ResultState.Loading)
                    }.doOnFailure {
                        _addNoteEventFlow.emit(ResultState.Failure(Throwable(it)))
                    }.doOnSuccess {
                        _addNoteEventFlow.emit(ResultState.Success(it))
                    }.collect()
            }

            is NoteEvent.AddUserDetailEvent -> {
                noteRepository.addUserDetails(event.auth)
                    .doOnLoading {
                        _addUserDetailEventFlow.emit(ResultState.Loading)
                    }.doOnFailure {
                        _addUserDetailEventFlow.emit(ResultState.Failure(Throwable(it)))
                    }.doOnSuccess {
                        _addUserDetailEventFlow.emit(ResultState.Success(it))
                    }.collect()
            }

            is NoteEvent.DeleteNoteEvent -> {}
            NoteEvent.GetNoteEvent -> {
                noteRepository.getNotes()
                    .doOnLoading {
                        _showNotesEventFlow.value = ComposeUi(
                            isLoading = true
                        )
                    }.doOnFailure {
                        _showNotesEventFlow.value = ComposeUi(
                            error = it?.message ?: SOMETHING_WET_WRONG
                        )
                    }.doOnSuccess {
                        _showNotesEventFlow.value = ComposeUi(
                            data = it
                        )
                    }.collect()
            }

            is NoteEvent.LoginUserEvent -> {
                noteRepository.loginUser(event.auth)
                    .doOnLoading {
                        _loginUserEventFlow.emit(ResultState.Loading)
                    }.doOnFailure {
                        _loginUserEventFlow.emit(ResultState.Failure(Throwable(it)))
                    }.doOnSuccess {
                        _loginUserEventFlow.emit(ResultState.Success(it))
                    }.collect()
            }

            is NoteEvent.RegisterUserEvent -> {
                noteRepository.createUser(event.auth)
                    .doOnLoading {
                        _createUserEventFlow.emit(ResultState.Loading)
                    }.doOnFailure {
                        _createUserEventFlow.emit(ResultState.Failure(Throwable(it)))
                    }.doOnSuccess {
                        _createUserEventFlow.emit(ResultState.Success(it))
                    }.collect()

            }

            is NoteEvent.UpdateNoteEvent -> {

            }

            NoteEvent.SignOutEvent -> noteRepository.signOut()
        }
    }

}

data class ComposeUi<T>(
    val data: List<T> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)

sealed class NoteEvent {

    data class RegisterUserEvent(val auth: Auth) : NoteEvent()
    data class LoginUserEvent(val auth: Auth) : NoteEvent()
    data class AddUserDetailEvent(val auth: Auth) : NoteEvent()
    data class AddNoteEvent(val note: Note) : NoteEvent()
    data object GetNoteEvent : NoteEvent()
    data class DeleteNoteEvent(val id: String) : NoteEvent()
    data class UpdateNoteEvent(val note: NoteResponse) : NoteEvent()
    data object SignOutEvent : NoteEvent()
}