package com.example.notesapp.adapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.models.Note
import com.example.notesapp.utility.Converters

class NotesAdapter(
    private val context: Context,
    private val listener: NotesClickListener
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val notesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()
    private val noteColors: MutableMap<String, Int> = HashMap()

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notesLayout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val noteTv = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
        val imageView = itemView.findViewById<ImageView>(R.id.iv_image)

        init {
            notesLayout.setOnLongClickListener {
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true
        holder.noteTv.text = currentNote.note
        holder.noteTv.isSelected = true
        holder.date.text = currentNote.date
        holder.date.isSelected = true


        if (currentNote.image != null) {
            holder.imageView.visibility = View.VISIBLE
            val bitmap = BitmapFactory.decodeByteArray(currentNote.image, 0, currentNote.image!!.size)
            holder.imageView.setImageBitmap(bitmap)
        } else {

            holder.imageView.visibility = View.GONE
        }

        val noteId = currentNote.id
        val color = noteColors.getOrPut(noteId.toString()) { randomColor() }

        holder.notesLayout.setCardBackgroundColor(
            ContextCompat.getColor(context, color)
        )

        holder.notesLayout.setOnClickListener {
            listener.onItemClicked(notesList[holder.adapterPosition])
        }

        holder.notesLayout.setOnLongClickListener {
            listener.onLongItemClicked(
                notesList[holder.adapterPosition],
                holder.notesLayout
            )
            true
        }
    }



    fun updateList(newList: List<Note>) {
        fullList.clear()
        fullList.addAll(newList)

        notesList.clear()
        notesList.addAll(fullList)
        notesList.reverse()

        setInitialColors()

        notifyDataSetChanged()
    }

    fun filterList(search: String) {
        notesList.clear()
        for (item in fullList) {
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true
            ) {
                notesList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    private fun randomColor(): Int {
        val list = listOf(
            R.color.Notecolor1,
            R.color.Notecolor2,
            R.color.Noteccoool,
            R.color.Notecolor3,
            R.color.orange,
            R.color.Notecolor5,
            R.color.Notecolor4,
            R.color.Notecolor11,
            R.color.Notecolor6,
            R.color.Notecolor7,
            R.color.Notecolor8,
            R.color.Notecolor9,
            R.color.Notecolor10,
            R.color.yellow,
        )
        return list.random()
    }


    private fun setInitialColors() {
        noteColors.clear()
        for (note in fullList) {
            noteColors[note.id.toString()] = randomColor()
        }
    }

    interface NotesClickListener {
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)
    }
}




////working code
//package com.example.notesapp.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.cardview.widget.CardView
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import com.example.notesapp.R
//import com.example.notesapp.models.Note
//
//class NotesAdapter(
//    private val context: Context,
//    private val listener: NotesClickListener
//) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
//
//    private val notesList = ArrayList<Note>()
//    private val fullList = ArrayList<Note>()
//    private val noteColors: MutableMap<String, Int> = HashMap()
//
//    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val notesLayout = itemView.findViewById<CardView>(R.id.card_layout)
//        val title = itemView.findViewById<TextView>(R.id.tv_title)
//        val noteTv = itemView.findViewById<TextView>(R.id.tv_note)
//        val date = itemView.findViewById<TextView>(R.id.tv_date)
//
//        init {
//            notesLayout.setOnLongClickListener {
//                true
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
//        return NoteViewHolder(
//            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return notesList.size
//    }
//
//    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
//        val currentNote = notesList[position]
//        holder.title.text = currentNote.title
//        holder.title.isSelected = true
//        holder.noteTv.text = currentNote.note
//        holder.noteTv.isSelected = true
//        holder.date.text = currentNote.date
//        holder.date.isSelected = true
//
//
//        val noteId = currentNote.id
//        val color = noteColors.getOrPut(noteId.toString()) { randomColor() }
//
//
//        holder.notesLayout.setCardBackgroundColor(
//            ContextCompat.getColor(context, color)
//        )
//
//        holder.notesLayout.setOnClickListener {
//            listener.onItemClicked(notesList[holder.adapterPosition])
//        }
//
//        holder.notesLayout.setOnLongClickListener {
//            listener.onLongItemClicked(
//                notesList[holder.adapterPosition],
//                holder.notesLayout
//            )
//            true
//        }
//    }
//
//    fun updateList(newList: List<Note>) {
//        fullList.clear()
//        fullList.addAll(newList)
//
//        notesList.clear()
//        notesList.addAll(fullList)
//        notesList.reverse()
//
//        // Set the initial colors when updating the list
//        setInitialColors()
//
//        notifyDataSetChanged()
//    }
//
//    fun filterList(search: String) {
//        notesList.clear()
//        for (item in fullList) {
//            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
//                item.note?.lowercase()?.contains(search.lowercase()) == true
//            ) {
//                notesList.add(item)
//            }
//        }
//        notifyDataSetChanged()
//    }
//
//    private fun randomColor(): Int {
//        val list = listOf(
//            R.color.Notecolor1,
//            R.color.Notecolor2,
//            R.color.Noteccoool,
//            R.color.Notecolor3,
//            R.color.orange,
//            R.color.Notecolor5,
//            R.color.Notecolor4,
//            R.color.Notecolor11,
//            R.color.Notecolor6,
//            R.color.Notecolor7,
//            R.color.Notecolor8,
//            R.color.Notecolor9,
//            R.color.Notecolor10,
//            R.color.yellow,
//        )
//        return list.random()
//    }
//
//    // Add this function to set the initial colors for all notes
//    private fun setInitialColors() {
//        noteColors.clear()
//        for (note in fullList) {
//            noteColors[note.id.toString()] = randomColor()
//
//        }
//    }
//
//    interface NotesClickListener {
//        fun onItemClicked(note: Note)
//        fun onLongItemClicked(note: Note, cardView: CardView)
//    }
//}
//
