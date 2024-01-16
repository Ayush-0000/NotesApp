package com.example.notesapp.models

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.database.NotesRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository
    val allNotes: LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
        saveNoteToFirestore(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {

        repository.update(note)
        saveNoteToFirestore(note)
    }

    fun saveNoteToFirestore(note: Note) {
        val firestore = FirebaseFirestore.getInstance()
        val noteCollection = firestore.collection("notes")

        noteCollection.add(note)
            .addOnSuccessListener { documentReference ->
                val generatedId = documentReference.id
                Toast.makeText(getApplication(), "Successfully saved ", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    getApplication(),
                    "Failed to upload to Firestore: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}



