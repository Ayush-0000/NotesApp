package com.example.notesapp.models
import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "Title") val title: String?,
    @ColumnInfo(name = "Note") val note: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "image") val image: ByteArray?
) : java.io.Serializable






//package com.example.notesapp.models
//
//import android.icu.text.CaseMap.Title
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import java.sql.RowId
//import java.util.jar.Attributes.Name
//@Entity(tableName="notes_table")
//data class Note(
//   @PrimaryKey(autoGenerate = true) val id:Int?,
//    @ColumnInfo(name="Title")val title:String?,
//   @ColumnInfo(name = "Note") val note: String?,
//    @ColumnInfo(name="date")val date: String?,
//
//) : java.io.Serializable
