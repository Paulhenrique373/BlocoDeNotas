package com.example.blocodenotas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val context: Context,
    private val notes: MutableList<Note>
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtTitle)
        val description: TextView = itemView.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.title.text = note.titulo
        holder.description.text = note.descricao

        holder.itemView.setOnClickListener {
            val intent = Intent(context, AddEditNoteActivity::class.java)
            intent.putExtra("id", note.id)
            intent.putExtra("title", note.titulo)
            intent.putExtra("description", note.descricao)
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            val popup = PopupMenu(context, holder.itemView)
            popup.menu.add("Excluir")

            popup.setOnMenuItemClickListener {
                val db = DatabaseHelper(context)
                db.deleteNote(note.id)
                notes.removeAt(position)
                notifyItemRemoved(position)
                true
            }

            popup.show()
            true
        }
    }
}