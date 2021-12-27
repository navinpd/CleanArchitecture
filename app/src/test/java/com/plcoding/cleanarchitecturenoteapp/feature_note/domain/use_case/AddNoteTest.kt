package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.plcoding.cleanarchitecturenoteapp.feature_app.data.repository.FakeRepository
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AddNoteTest {
    private lateinit var fakeRepository: FakeRepository
    private lateinit var addNote: AddNote
    @Before
    fun setUp() {
        fakeRepository = FakeRepository()
        addNote = AddNote(fakeRepository)
    }

    @Test(expected = InvalidNoteException::class)
    fun `verify error for empty note title`() = runBlocking {
        var note = Note(title = "", timeStamp = 12L, color = 1, content = "Hello Test")

        addNote.invoke(note)
    }

    @Test(expected = InvalidNoteException::class)
    fun `verify error for empty note content`() = runBlocking {
        var note = Note(title = "Hello Title", timeStamp = 12L, color = 1, content = "")

        addNote.invoke(note)
    }

    @Test
    fun `verify successful entry of note items`() = runBlocking {
        var note = Note(title = "Hello Title", timeStamp = 12L, color = 1, content = "Hello Content", id = 0)

        addNote.invoke(note)

        assertEquals(note.title, fakeRepository.getNoteById(0)?.title)
    }
}