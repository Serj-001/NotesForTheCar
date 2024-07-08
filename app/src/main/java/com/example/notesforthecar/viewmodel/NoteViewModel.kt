package com.example.notesforthecar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesforthecar.repository.Repository
import com.example.notesforthecar.room.NoteEntity
import kotlinx.coroutines.launch

class NoteViewModel(val repository: Repository): ViewModel() {

    fun addNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.addNote(note)
        }
    }

    val notes = repository.getAllNotes()

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }
}