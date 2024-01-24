//package com.example.notesapp.database
//
//import android.content.Context
//import com.example.notesapp.models.Note
//import com.example.notesapp.utility.DATABASE_NAME
//
//class NotesRepository private constructor(context: Context) {
//
//    private val noteDao: NoteDao
//
//    init {
//        val noteDatabase = NoteDatabase.getDatabase(context)
//        noteDao = noteDatabase.getNoteDao()
//    }
//
//    suspend fun insert(note: Note) {
//        noteDao.insert(note)
//    }
//
//    suspend fun delete(note: Note) {
//        noteDao.delete(note)
//    }
//
//    suspend fun update(note: Note) {
//        noteDao.update(note)
//    }
//
//    fun getAllNotes(userId: String) = noteDao.getAllNotes(userId)
//
//    // Other methods as needed
//
//    companion object {
//        @Volatile
//        private var INSTANCE: NotesRepository? = null
//
//        fun getInstance(context: Context): NotesRepository {
//            return INSTANCE ?: synchronized(this) {
//                val instance = NotesRepository(context)
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}






package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notesapp.models.Note
import com.example.notesapp.utility.Converters
import com.example.notesapp.utility.DATABASE_NAME

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NoteDatabase:RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    companion object {

        @Volatile
        private var INSTANCE: NoteDatabase? = null
        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // You may want to choose a more suitable migration strategy
                    .build()

                INSTANCE = instance
                instance
            }
        }

    }
}




