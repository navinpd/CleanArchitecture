package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util

sealed class Screen(val route : String) {

    object NoteScreen : Screen("note_screen")

    object AddEditNoteScreen : Screen("edit_note_screen")
}