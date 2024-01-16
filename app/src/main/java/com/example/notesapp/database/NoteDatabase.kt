package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.models.Note
import com.example.notesapp.utility.DATABASE_NAME

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)

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




