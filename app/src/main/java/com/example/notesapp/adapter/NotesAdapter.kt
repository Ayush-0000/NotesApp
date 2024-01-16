package com.example.notesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.models.Note

class NotesAdapter(private val context: Context, private val listener: NotesClickListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val notesLayout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val noteTv = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)

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
        return NotesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true
        holder.noteTv.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notesLayout.setCardBackgroundColor(
            holder.itemView.resources.getColor(
                randomColor(),
                null
            )
        )
        holder.notesLayout.setOnClickListener {
            listener.onItemClicked(NotesList[holder.adapterPosition])
        }

        holder.notesLayout.setOnLongClickListener{
            listener.onLongItemClicked(NotesList[holder.adapterPosition],holder.notesLayout)
            true
        }
    }

    fun updateList(newList: List<Note>) {
        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)
        NotesList.reverse()
        notifyDataSetChanged()
    }

    fun filterList(search: String) {
        NotesList.clear()
        for (item in fullList) {
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true
            ) {
                NotesList.add(item)
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



    interface NotesClickListener {
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)
    }
}









