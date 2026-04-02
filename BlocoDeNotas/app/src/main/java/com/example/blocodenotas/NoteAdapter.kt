package com.example.blocodenotas

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class NoteAdapter(
    private val context: Context,
    val notes: MutableList<Note> // 👈 tirei o private pra acessar na Activity
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // 🔥 LISTA DE SELECIONADOS
    val selectedNotes = mutableListOf<Note>()
    var isSelectionMode = false

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtTitle)
        val description: TextView = itemView.findViewById(R.id.txtDescription)
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardNote)
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

        val colors = listOf(
            Color.parseColor("#FFE7C2"),
            Color.parseColor("#D9F4FF"),
            Color.parseColor("#F6D8FF"),
            Color.parseColor("#DFFFD8"),
            Color.parseColor("#FFF3C7")
        )

        holder.cardView.setCardBackgroundColor(colors[position % colors.size])

        // 🔥 VISUAL DE SELEÇÃO
        if (selectedNotes.contains(note)) {
            holder.itemView.alpha = 0.5f
        } else {
            holder.itemView.alpha = 1.0f
        }

        // 🔥 CLIQUE NORMAL
        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                toggleSelection(note)
            } else {
                val intent = Intent(context, AddEditNoteActivity::class.java)
                intent.putExtra("id", note.id)
                intent.putExtra("title", note.titulo)
                intent.putExtra("description", note.descricao)
                context.startActivity(intent)
            }
        }

        // 🔥 SEGURAR PARA ATIVAR SELEÇÃO
        holder.itemView.setOnLongClickListener {
            isSelectionMode = true
            toggleSelection(note)
            true
        }
    }

    // 🔥 FUNÇÃO DE SELEÇÃO
    fun toggleSelection(note: Note) {
        if (selectedNotes.contains(note)) {
            selectedNotes.remove(note)
        } else {
            selectedNotes.add(note)
        }
        notifyDataSetChanged()
    }

    // 🔥 LIMPAR SELEÇÃO
    fun clearSelection() {
        selectedNotes.clear()
        isSelectionMode = false
        notifyDataSetChanged()
    }
}