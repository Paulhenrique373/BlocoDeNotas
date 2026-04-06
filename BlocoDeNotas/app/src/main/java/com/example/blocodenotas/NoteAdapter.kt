package com.example.blocodenotas

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class NoteAdapter(
    private val context: Context,
    private var notes: MutableList<Note>,
    private val onSelectionChanged: (Boolean) -> Unit // 🔥 NOVO
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    val selectedNotes = mutableListOf<Note>()
    var isSelectionMode = false

    private val db = DatabaseHelper(context)

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtTitle)
        val description: TextView = itemView.findViewById(R.id.txtDescription)
        val txtData: TextView = itemView.findViewById(R.id.txtData)
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardNote)
        val btnPin: ImageView = itemView.findViewById(R.id.btnPin)
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
        holder.txtData.text = note.data

        val colors = listOf(
            Color.parseColor("#FFE7C2"),
            Color.parseColor("#D9F4FF"),
            Color.parseColor("#F6D8FF"),
            Color.parseColor("#DFFFD8"),
            Color.parseColor("#FFF3C7")
        )

        // 🎨 cor padrão
        holder.cardView.setCardBackgroundColor(colors[position % colors.size])

        // 🔥 SELEÇÃO BONITA
        if (selectedNotes.contains(note)) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#EDE7FF"))
            holder.cardView.strokeWidth = 4
            holder.cardView.strokeColor = Color.parseColor("#7B61FF")
        } else {
            holder.cardView.setCardBackgroundColor(colors[position % colors.size])
            holder.cardView.strokeWidth = 0
        }

        // 📌 FIXAR
        if (note.fixada == 1) {
            holder.btnPin.setImageResource(android.R.drawable.star_big_on)
        } else {
            holder.btnPin.setImageResource(android.R.drawable.star_big_off)
        }

        holder.btnPin.setOnClickListener {
            note.fixada = if (note.fixada == 1) 0 else 1
            db.updateNote(note)

            notes.sortWith(compareByDescending<Note> { it.fixada }.thenByDescending { it.id })

            notifyDataSetChanged()
        }

        // 🔥 clique normal
        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                toggleSelection(note)
            } else {
                val intent = Intent(context, AddEditNoteActivity::class.java)
                intent.putExtra("id", note.id)
                intent.putExtra("title", note.titulo)
                intent.putExtra("description", note.descricao)
                intent.putExtra("cor", note.cor)
                context.startActivity(intent)
            }
        }

        // 🔥 segurar para selecionar
        holder.itemView.setOnLongClickListener {
            isSelectionMode = true
            toggleSelection(note)
            true
        }
    }

    fun toggleSelection(note: Note) {
        if (selectedNotes.contains(note)) {
            selectedNotes.remove(note)
        } else {
            selectedNotes.add(note)
        }

        // 🔥 AVISA A ACTIVITY
        onSelectionChanged(selectedNotes.isNotEmpty())

        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedNotes.clear()
        isSelectionMode = false

        onSelectionChanged(false) // 🔥 AVISA QUE LIMPOU

        notifyDataSetChanged()
    }

    fun updateList(newList: MutableList<Note>) {
        notes = newList
        notifyDataSetChanged()
    }
}