package com.example.notesapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.databinding.ActivityAddNoteBinding
import com.example.notesapp.models.Note
import com.example.notesapp.models.NoteViewModel
import com.example.notesapp.utility.Converters
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note: Note
    private lateinit var oldNote: Note
    private var isUpdate = false
    private var selectedImage: Bitmap? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {
                val inputStream: InputStream? = contentResolver.openInputStream(it)
                selectedImage = BitmapFactory.decodeStream(inputStream)
                // Display or handle the selected image as needed
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Inside onCreate method
        val viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)


        try {
            oldNote = intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val noteDesc = binding.etNote.text.toString()

            if (title.isNotEmpty() || noteDesc.isNotEmpty()) {
                val formatter = SimpleDateFormat("d MMM yy HH:mm a", Locale.getDefault())

                note = if (isUpdate) {
                    // Ensure selectedImage is not null before calling fromBitmap
                    val imageByteArray: ByteArray? = selectedImage?.let { bitmap ->
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        stream.toByteArray()
                    }
                    Note(
                        oldNote.id, title, noteDesc, formatter.format(Date()), imageByteArray
                    )
                } else {
                    // Ensure selectedImage is not null before calling fromBitmap
                    val imageByteArray: ByteArray? = selectedImage?.let { bitmap ->
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        stream.toByteArray()
                    }
                    Note(
                        null, title, noteDesc, formatter.format(Date()), imageByteArray
                    )
                }

                // Use ViewModel to insert or update note with image
//                if (isUpdate) {
//                    viewModel.updateNoteWithImage(note, selectedImage)
//                } else {
//                    viewModel.insertNoteWithImage(note, selectedImage)
//                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddNote, "Please Enter Some Data", Toast.LENGTH_SHORT).show()
            }
        }


        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }


    }

}







//working
//package com.example.notesapp
//
//import android.app.Activity
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.example.notesapp.models.Note
//import android.widget.Toast
//import com.example.notesapp.databinding.ActivityAddNoteBinding
//import java.lang.Exception
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//
//class AddNote : AppCompatActivity() {
//    private lateinit var binding: ActivityAddNoteBinding
//    private lateinit var note: Note
//    private lateinit var old_note:Note
//    private var isUpdate=false
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding=ActivityAddNoteBinding.inflate(layoutInflater)
//
//        setContentView(binding.root)
//
//        try {
//            old_note=intent.getSerializableExtra("current_note") as Note
//            binding.etTitle.setText(old_note.title)
//            binding.etNote.setText(old_note.note)
//            isUpdate=true
//        }catch (e : Exception){
//            e.printStackTrace()
//        }
//        binding.imgCheck.setOnClickListener{
//            val title=binding.etTitle.text.toString()
//            val note_desc =binding.etNote.text.toString()
//            if (title.isNotEmpty()|| note_desc.isNotEmpty()){
//                val formatter = SimpleDateFormat("d MMM yy HH:mm a", Locale.getDefault())
//
//                if (isUpdate){
//                    note=Note(
//                        old_note.id,title,note_desc,formatter.format(Date())
//                    )
//                }else{
//
//                    note=Note(
//                        null,title,note_desc,formatter.format(Date())
//                    )
//                }
//                val intent=Intent()
//                intent.putExtra("note",note)
//                setResult(Activity.RESULT_OK,intent)
//                finish()
//            }
//            else{
//                Toast.makeText(this@AddNote, "Please Enter Some Data", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//        }
//       binding.imgBackArrow.setOnClickListener{
//           onBackPressed()
//      }
//
//
//    }
//}