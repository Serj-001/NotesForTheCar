package com.example.notesforthecar.repository

import com.example.notesforthecar.room.NoteEntity
import com.example.notesforthecar.room.NotesDB

class Repository(val notesDB: NotesDB) {

    suspend fun addNote(noteEntity: NoteEntity) {
        notesDB.noteDao().addNote(noteEntity)
    }

    fun getAllNotes() = notesDB.noteDao().getAllNotes()

    suspend fun deleteNote(noteEntity: NoteEntity) {
        notesDB.noteDao().deleteNote(noteEntity)
    }

    suspend fun updateNote(noteEntity: NoteEntity) {
        notesDB.noteDao().updateNote(noteEntity)
    }
}