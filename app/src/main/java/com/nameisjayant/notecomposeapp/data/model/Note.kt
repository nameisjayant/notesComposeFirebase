package com.nameisjayant.notecomposeapp.data.model

data class Note(
    val title: String? = "",
    val description: String? = ""
)

data class NoteResponse(
    val id: String? = "",
    val note: Note? = null
)