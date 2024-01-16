package com.example.notesapp
import com.example.notesapp.models.NoteViewModel
import android.app.Activity
import android.content.Intent
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
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.models.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp




class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter
    private lateinit var selectedNote: Note
    private lateinit var auth: FirebaseAuth

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val note = result.data?.getSerializableExtra("note") as? Note
            if (note != null) {
                viewModel.updateNote(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
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

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        Log.d("PopupMenu", "popUpDisplay called")
        Toast.makeText(this, "clicked now", Toast.LENGTH_SHORT).show()
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

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

