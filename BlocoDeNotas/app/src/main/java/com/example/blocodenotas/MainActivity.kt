package com.example.blocodenotas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var db: DatabaseHelper
    private lateinit var btnDelete: Button // 🔥 NOVO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        btnDelete = findViewById(R.id.btnDelete) // 🔥 IMPORTANTE

        db = DatabaseHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditNoteActivity::class.java))
        }

        // 🔥 BOTÃO EXCLUIR VÁRIOS
        btnDelete.setOnClickListener {
            val selected = adapter.selectedNotes

            for (note in selected) {
                db.deleteNote(note.id)
                noteList.remove(note)
            }

            adapter.clearSelection()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()

        noteList = db.getAllNotes()
        adapter = NoteAdapter(this, noteList)
        recyclerView.adapter = adapter
    }
}