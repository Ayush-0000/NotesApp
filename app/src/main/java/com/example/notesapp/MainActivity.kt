package com.example.notesapp
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notesapp.AddNote
import com.example.notesapp.LoginActivity
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.models.Note
import com.example.notesapp.models.NoteViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.Date
private val UPDATE_NOTE_REQUEST_CODE = 1

class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter
    private lateinit var selectedNote: Note
    private lateinit var auth: FirebaseAuth

    // Request code for image selection
    private val getImageContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleImageSelection(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }
        database = NoteDatabase.getDatabase(this)
        auth = FirebaseAuth.getInstance()
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth1: FirebaseAuth

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().reference
        auth1 = FirebaseAuth.getInstance()
        val currentUser = auth1.currentUser

        currentUser?.let { user ->
            binding.fbAddImageNote.setOnClickListener {
                openImageSelection()
            }
        }
        currentUser?.let { user ->
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null) {
                    viewModel.insertNote(note)
                }
            }
        }
            binding.fbAddNote.setOnClickListener {
                val intent = Intent(this, AddNote::class.java)


                getContent.launch(intent)
            }

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }
                return true
            }
        })

        binding.SignOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun openImageSelection() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        getImageContent.launch(intent)
    }

    // Inside handleImageSelection function
    private fun handleImageSelection(uri: android.net.Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val imageBitmap = BitmapFactory.decodeStream(inputStream)
                val note = createNoteWithImage(imageBitmap)
                viewModel.insertNote(note)
            } else {
                Toast.makeText(this, "Failed to open image stream", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun createNoteWithImage(imageBitmap: Bitmap): Note {
        // Convert the Bitmap to a ByteArray
        val imageByteArray = convertBitmapToByteArray(imageBitmap)

        return Note(
            id = null,
            title = "Selected image",
            note = "Do not click",
            date = " ",
            image = imageByteArray
        )
    }

    // Function to convert Bitmap to ByteArray
    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPDATE_NOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Handle the result here if needed
            val updatedNote = data?.getSerializableExtra("note") as? Note
            updatedNote?.let {
                viewModel.updateNote(it)
            }
        }
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        Log.d("PopupMenu", "popUpDisplay called")
        val popup = PopupMenu(this, cardView)
        popup.menuInflater.inflate(R.menu.pop_up_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            onMenuItemClick(item)
            true
        }

        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete_note -> {
                viewModel.deleteNote(selectedNote)

                return true
            }

        }

        return false
    }
    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        startActivityForResult(intent, UPDATE_NOTE_REQUEST_CODE)
    }

    private fun signOut() {
        viewModel.signOut()
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}



//working code
//package com.example.notesapp
//
//import com.example.notesapp.models.NoteViewModel
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.MenuItem
//import android.widget.PopupMenu
//import android.widget.SearchView
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.cardview.widget.CardView
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.GridLayoutManager
//import com.example.notesapp.adapter.NotesAdapter
//import com.example.notesapp.database.NoteDatabase
//import com.example.notesapp.databinding.ActivityMainBinding
//import com.example.notesapp.models.Note
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.FirebaseApp
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//
//
//class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener {
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var database: NoteDatabase
//    private lateinit var viewModel: NoteViewModel
//    private lateinit var adapter: NotesAdapter
//    private lateinit var selectedNote: Note
//    private lateinit var auth: FirebaseAuth
//
//    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val note = result.data?.getSerializableExtra("note") as? Note
//            if (note != null) {
//                viewModel.updateNote(note)
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        FirebaseApp.initializeApp(this)
//        initUI()
//
//        viewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        ).get(NoteViewModel::class.java)
//        viewModel.allNotes.observe(this) { list ->
//            list?.let {
//                adapter.updateList(list)
//            }
//        }
//        database = NoteDatabase.getDatabase(this)
//        auth = FirebaseAuth.getInstance()
//    }
//    private lateinit var databaseReference: DatabaseReference
//    private lateinit var auth1: FirebaseAuth
//
//
//    private fun initUI() {
//        binding.recyclerView.setHasFixedSize(true)
//        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
//        adapter = NotesAdapter(this, this)
//        binding.recyclerView.adapter = adapter
//
//
//        databaseReference = FirebaseDatabase.getInstance().reference
//        auth1 = FirebaseAuth.getInstance()
//        val currentUser = auth1.currentUser
//        currentUser?.let { user ->
//        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val note = result.data?.getSerializableExtra("note") as? Note
//                if (note != null) {
//                    viewModel.insertNote(note)
//                }
//            }
//        }
//            binding.fbAddNote.setOnClickListener {
//                val intent = Intent(this, AddNote::class.java)
//
//
//                getContent.launch(intent)
//            }
//
//        }
//
//
//
//
//
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != null) {
//                    adapter.filterList(newText)
//                }
//                return true
//            }
//        })
//
//
//        binding.SignOutButton.setOnClickListener {
//            signOut()
//        }
//    }
//
//    override fun onItemClicked(note: Note) {
//        val intent = Intent(this@MainActivity, AddNote::class.java)
//        intent.putExtra("current_note", note)
//        updateNote.launch(intent)
//    }
//
//    override fun onLongItemClicked(note: Note, cardView: CardView) {
//        selectedNote = note
//        popUpDisplay(cardView)
//    }
//
//    private fun popUpDisplay(cardView: CardView) {
//        Log.d("PopupMenu", "popUpDisplay called")
//        Toast.makeText(this, "clicked now", Toast.LENGTH_SHORT).show()
//        val popup = PopupMenu(this, cardView)
//        popup.menuInflater.inflate(R.menu.pop_up_menu, popup.menu)
//
//        popup.setOnMenuItemClickListener { item ->
//            onMenuItemClick(item)
//            true
//        }
//
//        popup.show()
//    }
//
//    override fun onMenuItemClick(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            R.id.delete_note -> {
//                viewModel.deleteNote(selectedNote)
//                return true
//            }
//
//        }
//        return false
//    }
//    private fun signOut() {
//        viewModel.signOut()
//        auth.signOut()
//        startActivity(Intent(this, LoginActivity::class.java))
//        finish()
//    }
//
////    private fun signOut() {
////        auth.signOut()
////        startActivity(Intent(this, LoginActivity::class.java))
////        finish()
////    }
//}

