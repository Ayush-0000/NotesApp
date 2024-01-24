package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notesapp.models.Note
import com.example.notesapp.utility.Converters
import com.example.notesapp.utility.DATABASE_NAME

@Database(entities = [Note::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {

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
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}






//working code
//package com.example.notesapp.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.room.TypeConverters
//import com.example.notesapp.models.Note
//import com.example.notesapp.utility.Converters
//import com.example.notesapp.utility.DATABASE_NAME
//
//@Database(entities = arrayOf(Note::class), version = 2, exportSchema = false)
//@TypeConverters(Converters::class)
//abstract class NoteDatabase:RoomDatabase() {
//    abstract fun getNoteDao(): NoteDao
//
//    companion object {
//
//        @Volatile
//        private var INSTANCE: NoteDatabase? = null
//        fun getDatabase(context: Context): NoteDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    NoteDatabase::class.java,
//                    DATABASE_NAME
//                )
//                    .fallbackToDestructiveMigration() // You may want to choose a more suitable migration strategy
//                    .build()
//
//                INSTANCE = instance
//                instance
//            }
//        }
//
//    }
//}




