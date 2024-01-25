package com.example.notesapp.database

import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.notesapp.models.Note

class NotesRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)


    }

    suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }

    suspend fun update(note: Note) {
        noteDao.update(note.id, note.title,note.note, note.image)
    }

    suspend fun updateNotesFromFirestore(notesList: List<Note>) {
        noteDao.deleteAllNotes()
        noteDao.insertAll(notesList)
    }

    suspend fun insertNoteWithImage(note: Note) {
        noteDao.insert(note)
    }

    suspend fun updateNoteWithImage(note: Note) {
        noteDao.update(note.id, note.title, note.note, note.image)
    }
}


//working code
//package com.example.notesapp.database
//
//import androidx.lifecycle.LiveData
//import com.example.notesapp.models.Note
//
//class NotesRepository(private val noteDao:NoteDao) {
//    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()
//
//    suspend fun insert(note: Note) {
//        noteDao.insert(note)
//    }
//
//    suspend fun delete(note: Note) {
//        noteDao.delete(note)
//
//
//    }
//    suspend fun deleteAllNotes(){
//    noteDao.deleteAllNotes()
//     }
//    suspend fun update(note: Note) {
//        noteDao.update(note.id, note.title, note.note)
//
//
//    }
//    suspend fun updateNotesFromFirestore(notesList: List<Note>) {
//        noteDao.deleteAllNotes()
//        noteDao.insertAll(notesList)
//    }
//}