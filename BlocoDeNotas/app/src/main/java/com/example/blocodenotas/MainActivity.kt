package com.example.blocodenotas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var db: DatabaseHelper
    private lateinit var btnDelete: Button
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🔎 TOOLBAR
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        btnDelete = findViewById(R.id.btnDelete)

        db = DatabaseHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditNoteActivity::class.java))
        }

        // 🔥 COMEÇA ESCONDIDO
        btnDelete.visibility = View.GONE

        // 🗑 EXCLUIR
        btnDelete.setOnClickListener {
            val selected = adapter.selectedNotes.toList()

            for (note in selected) {
                db.deleteNote(note.id)
                noteList.remove(note)
            }

            adapter.clearSelection()
            adapter.updateList(noteList)

            // 🔥 RESETA TOOLBAR
            toolbar.title = "Minhas Notas"
            btnDelete.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        noteList = db.getAllNotes()

        adapter = NoteAdapter(this, noteList) { selecionou ->

            val quantidade = adapter.selectedNotes.size

            if (selecionou) {
                btnDelete.visibility = View.VISIBLE

                // 🔥 TEXTO NO TOPO
                toolbar.title = if (quantidade == 1) {
                    "1 selecionada"
                } else {
                    "$quantidade selecionadas"
                }

            } else {
                btnDelete.visibility = View.GONE
                toolbar.title = "Minhas Notas"
            }
        }

        recyclerView.adapter = adapter
    }

    // 🔎 PESQUISA
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "Pesquisar notas..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarNotas(newText ?: "")
                return true
            }
        })

        return true
    }

    private fun filtrarNotas(texto: String) {

        if (texto.isEmpty()) {
            adapter.updateList(noteList)
            return
        }

        val listaFiltrada = noteList.filter {
            it.titulo.contains(texto, true) ||
                    it.descricao.contains(texto, true)
        }

        adapter.updateList(listaFiltrada.toMutableList())
    }
}