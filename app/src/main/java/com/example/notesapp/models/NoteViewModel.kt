//package com.example.notesapp.models
//import android.app.Application
//import android.graphics.Bitmap
//import android.net.Uri
//import android.widget.Toast
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.viewModelScope
//import com.example.notesapp.database.NoteDatabase
//import com.example.notesapp.database.NotesRepository
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.firestore.FieldValue
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import java.io.ByteArrayOutputStream
//import java.text.SimpleDateFormat
//import java.util.Locale
//import java.util.UUID
//
//class NoteViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository: NotesRepository
//    val allNotes: LiveData<List<Note>>
//
//    init {
//        val dao = NoteDatabase.getDatabase(application).getNoteDao()
//        repository = NotesRepository(dao)
//        allNotes = repository.allNotes
//    }
//
//    fun signOut() = viewModelScope.launch(Dispatchers.IO) {
//        repository.deleteAllNotes()
//    }
//
//    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
//        repository.delete(note)
//        note.id?.let { deleteNoteFromFirestore(it.toLong()) }
//    }
//
//    fun insertNote(note: Note, image: Bitmap?, imageUri: Uri?) = viewModelScope.launch(Dispatchers.IO) {
//        val imageUrl = uploadImageToFirebaseStorage(imageUri)
//        note.imageURL = imageUrl
//        repository.insert(note)
//        saveNoteToFirestore(note)
//    }
//
//    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
//        repository.update(note)
//        saveNoteToFirestore(note)
//    }
//    private suspend fun uploadImageToFirebaseStorage(imageUri: Uri?): String? {
//        return imageUri?.let {
//            val storageRef = FirebaseStorage.getInstance().reference
//            val imageFileName = "images/${UUID.randomUUID()}.jpg"
//            val imageRef = storageRef.child(imageFileName)
//            val uploadTask = imageRef.putFile(it)
//
//            return@let try {
//                uploadTask.await()
//                imageRef.downloadUrl.await().toString()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                null
//            }
//        }
//    }
//    private fun deleteNoteFromFirestore(noteId: Long) {
//        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
//
//        user?.let {
//            val userUid = it.uid
//            val firestore = FirebaseFirestore.getInstance()
//            val userCollection = firestore.collection("users").document(userUid)
//            val notesCollection = userCollection.collection("notes")
//            val noteref = notesCollection.document(noteId.toString())
//
//            noteref
//                .delete()
//                .addOnSuccessListener {
//                    Toast.makeText(getApplication(), "Note deleted from Firestore", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(
//                        getApplication(),
//                        "Failed to delete note from Firestore: ${e.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//        }
//    }
//
//    private suspend fun insertNoteWithImage(note: Note, image: Bitmap?) {
//        image?.let {
//            val imageByteArray = convertBitmapToByteArray(it)
//            val noteWithImage = Note(
//                id = note.id,
//                title = "Selected image",
//                note = "Do not click",
//                date = " ",
//                image = imageByteArray
//            )
//            repository.insertNoteWithImage(noteWithImage)
//            saveNoteToFirestore(noteWithImage)
//        }
//    }
//
//    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        return outputStream.toByteArray()
//    }
//
//    private fun saveNoteToFirestore(note: Note) {
//        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
//
//        if (user != null) {
//            val userUid = user.uid
//            val firestore = FirebaseFirestore.getInstance()
//            val userCollection = firestore.collection("users").document(userUid)
//            val notesCollection = userCollection.collection("notes")
//
//            val noteData = hashMapOf(
//                "title" to note.title,
//                "content" to note.note,
//                "timestamp" to FieldValue.serverTimestamp()
//            )
//
//            notesCollection.add(noteData)
//                .addOnSuccessListener { documentReference ->
//                    val generatedId = documentReference.id
//                    Toast.makeText(getApplication(), "Successfully saved ", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(
//                        getApplication(),
//                        "Failed to upload to Firestore: ${e.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//        } else {
//            Toast.makeText(getApplication(), "User not authenticated", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    fun getAllNotesFromFirestore() {
//        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
//
//        if (user != null) {
//            val userUid = user.uid
//            val firestore = FirebaseFirestore.getInstance()
//            val userCollection = firestore.collection("users").document(userUid)
//            val notesCollection = userCollection.collection("notes")
//
//            notesCollection.addSnapshotListener { snapshot, exception ->
//                if (exception != null) {
//                    // Handle error
//                    return@addSnapshotListener
//                }
//
//                val notesList = mutableListOf<Note>()
//
//                for (document in snapshot?.documents ?: emptyList()) {
//                    val title = document.getString("title")
//                    val content = document.getString("content")
//                    val timestamp = document.getTimestamp("timestamp")
//
//                    if (title != null && content != null && timestamp != null) {
//                        val note = Note(
//                            id = null,
//                            title = title,
//                            note = content,
//                            date = SimpleDateFormat("d MMM yy HH:mm a", Locale.getDefault()).format(timestamp.toDate()),
//                            image = null
//                        )
//                        notesList.add(note)
//                    }
//                }
//
//                updateLocalNotesList(notesList)
//            }
//        }
//    }
//
//    private fun updateLocalNotesList(notesList: List<Note>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.updateNotesFromFirestore(notesList)
//        }
//    }
//}
//




package com.example.notesapp.models

import android.app.Application
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.database.NotesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository
    val allNotes: LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes
    }

    fun signOut() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllNotes()
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)

        note.id?.let { deleteNoteFromFirestore(it.toLong()) }

    }


    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
        saveNoteToFirestore(note)

    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
        saveNoteToFirestore(note)
    }
    private fun deleteNoteFromFirestore(noteId: Long) {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        user?.let {
            val userUid = it.uid
            val firestore = FirebaseFirestore.getInstance()
            val userCollection = firestore.collection("users").document(userUid)
            val notesCollection = userCollection.collection("notes")
            val noteref = notesCollection.document(noteId.toString())

            noteref
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(getApplication(), "Note deleted from Firestore", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        getApplication(),
                        "Failed to delete note from Firestore: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
    fun insertNoteWithImage(note: Note, image: Bitmap?) = viewModelScope.launch(Dispatchers.IO) {
        // Convert the Bitmap image to ByteArray using your converter or utility class
        val imageByteArray = convertBitmapToByteArray(image)

        // Create a new Note object and update its image property
        val noteWithImage = Note(
            id = note.id,
            title = "Selected image",
            note = "Do not click",
            date = " ",
            image = imageByteArray
        )

        repository.insertNoteWithImage(noteWithImage)
        saveNoteToFirestore(noteWithImage)
    }

//    fun updateNoteWithImage(note: Note, image: Bitmap?) = viewModelScope.launch(Dispatchers.IO) {
//        // Convert the Bitmap image to ByteArray using your converter or utility class
//        val imageByteArray = convertBitmapToByteArray(image)
//
//        // Create a new Note object and update its image property
//        val noteWithImage = Note(
//            id = note.id,
//            title = "Selected image",
//            note = "Do not click",
//            date = " ",
//            image = imageByteArray
//        )
//
//        repository.updateNoteWithImage(noteWithImage)
//        saveNoteToFirestore(noteWithImage)
//    }




    private fun convertBitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        return bitmap?.let {
            val outputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.toByteArray()
        }
    }

    private fun saveNoteToFirestore(note: Note) {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val userUid = user.uid
            val firestore = FirebaseFirestore.getInstance()
            val userCollection = firestore.collection("users").document(userUid)
            val notesCollection = userCollection.collection("notes")

            val noteData = hashMapOf(
                "title" to note.title,
                "content" to note.note,
                "timestamp" to FieldValue.serverTimestamp()
            )

            notesCollection.add(noteData)
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
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(getApplication(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    fun getAllNotesFromFirestore() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val userUid = user.uid
            val firestore = FirebaseFirestore.getInstance()
            val userCollection = firestore.collection("users").document(userUid)
            val notesCollection = userCollection.collection("notes")

            notesCollection.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val notesList = mutableListOf<Note>()

                for (document in snapshot?.documents ?: emptyList()) {
                    val title = document.getString("title")
                    val content = document.getString("content")
                    val timestamp = document.getTimestamp("timestamp")

                    if (title != null && content != null && timestamp != null) {
                        val note = Note(
                            id = null,
                            title = title,
                            note = content,
                            date = SimpleDateFormat("d MMM yy HH:mm a", Locale.getDefault()).format(timestamp.toDate()),
                            image = null
                        )
                        notesList.add(note)
                    }
                }


                updateLocalNotesList(notesList)
            }
        }
    }

    private fun updateLocalNotesList(notesList: List<Note>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNotesFromFirestore(notesList)
        }
    }
}









////working code
//package com.example.notesapp.models
//
//import android.app.Application
//import android.widget.Toast
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.viewModelScope
//import com.example.notesapp.database.NoteDatabase
//import com.example.notesapp.database.NotesRepository
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.firestore.FieldValue
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//class NoteViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository: NotesRepository
//    val allNotes: LiveData<List<Note>>
//
//    init {
//        val dao = NoteDatabase.getDatabase(application).getNoteDao()
//        repository = NotesRepository(dao)
//        allNotes = repository.allNotes
//    }
//    fun signOut() = viewModelScope.launch(Dispatchers.IO) {
//        repository.deleteAllNotes()
//    }
//    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
//        repository.delete(note)
//    }
//
//    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insert(note)
//        saveNoteToFirestore(note)
//    }
//
//    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
//        repository.update(note)
//        saveNoteToFirestore(note)
//    }
//
//    private fun saveNoteToFirestore(note: Note) {
//        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
//
//        if (user != null) {
//            val userUid = user.uid
//            val firestore = FirebaseFirestore.getInstance()
//            val userCollection = firestore.collection("users").document(userUid)
//            val notesCollection = userCollection.collection("notes")
//
//            val noteData = hashMapOf(
//                "title" to note.title,
//                "content" to note.note,
//                "timestamp" to FieldValue.serverTimestamp()
//            )
//
//            notesCollection.add(noteData)
//                .addOnSuccessListener { documentReference ->
//                    val generatedId = documentReference.id
//                    Toast.makeText(getApplication(), "Successfully saved ", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(
//                        getApplication(),
//                        "Failed to upload to Firestore: ${e.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//        } else {
//            // Handle the case where the user is not authenticated
//            Toast.makeText(getApplication(), "User not authenticated", Toast.LENGTH_SHORT).show()
//        }
//    }
//    private fun deleteNoteFromFirestore(firestoreDocumentId: String) {
//        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
//
//        if (user != null) {
//            val userUid = user.uid
//            val firestore = FirebaseFirestore.getInstance()
//            val userCollection = firestore.collection("users").document(userUid)
//            val notesCollection = userCollection.collection("notes")
//
//            notesCollection.document(firestoreDocumentId).delete()
//                .addOnSuccessListener {
//                    // Handle success if needed
//                }
//                .addOnFailureListener { e ->
//                    // Handle failure if needed
//                }
//        }
//    }
//    fun getAllNotesFromFirestore() {
//        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
//
//        if (user != null) {
//            val userUid = user.uid
//            val firestore = FirebaseFirestore.getInstance()
//            val userCollection = firestore.collection("users").document(userUid)
//            val notesCollection = userCollection.collection("notes")
//
//            notesCollection.addSnapshotListener { snapshot, exception ->
//                if (exception != null) {
//                    // Handle error
//                    return@addSnapshotListener
//                }
//
//                val notesList = mutableListOf<Note>()
//
//                for (document in snapshot?.documents ?: emptyList()) {
//                    val title = document.getString("title")
//                    val content = document.getString("content")
//                    val timestamp = document.getTimestamp("timestamp")
//
//                    if (title != null && content != null && timestamp != null) {
//                        val note = Note(
//                            id = null,
//                            title = title,
//                            note = content,
//                            date = SimpleDateFormat("d MMM yy HH:mm a", Locale.getDefault()).format(timestamp.toDate()),
//
//                        )
//                        notesList.add(note)
//                    }
//                }
//
//                // Update the local LiveData with the fetched notes
//                updateLocalNotesList(notesList)
//            }
//        }
//    }
//
//    private fun updateLocalNotesList(notesList: List<Note>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.updateNotesFromFirestore(notesList)
//        }
//    }
//
//}






