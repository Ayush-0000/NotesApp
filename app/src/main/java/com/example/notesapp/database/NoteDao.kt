package com.example.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notesapp.models.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * from notes_table order by id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("UPDATE notes_table SET title=:title, note=:note, image=:image WHERE id=:id")
    suspend fun update(id: Int?, title: String?, note: String?, image: ByteArray?)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<Note>)
}

//@Dao
//interface NoteDao {
//    @Insert(onConflict =OnConflictStrategy.REPLACE)
//    suspend fun insert(note : Note)
//    @Delete
//    suspend fun delete(note:Note)
//    @Query("SELECT * from notes_table order by id ASC")
//    fun getAllNotes():LiveData<List<Note>>
//    @Query("UPDATE notes_table Set title=:title,note=:note WHERE id=:id")
//    suspend fun update(id:Int?,title:String?,note:String?)
//    @Query("DELETE FROM notes_table")
//    suspend fun deleteAllNotes()
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(notes: List<Note>)
//
//    }

