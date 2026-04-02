package com.example.blocodenotas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        db = DatabaseHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditNoteActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        noteList = db.getAllNotes()
        adapter = NoteAdapter(this, noteList)
        recyclerView.adapter = adapter
    }
}