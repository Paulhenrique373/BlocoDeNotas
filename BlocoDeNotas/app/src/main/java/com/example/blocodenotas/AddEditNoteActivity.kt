package com.example.blocodenotas

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var db: DatabaseHelper

    private var noteId = 0

    // 🔥 guardar valores antigos
    private var tituloAntigo = ""
    private var descricaoAntiga = ""

    // 🎨 cor da nota
    private var corSelecionada = "#FFE7C2"

    // ⏱️ controle de auto save
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        btnSave = findViewById(R.id.btnSave)

        db = DatabaseHelper(this)

        noteId = intent.getIntExtra("id", 0)

        // 🔥 EDITANDO NOTA
        if (noteId != 0) {
            tituloAntigo = intent.getStringExtra("title") ?: ""
            descricaoAntiga = intent.getStringExtra("description") ?: ""
            corSelecionada = intent.getStringExtra("cor") ?: "#FFE7C2"

            edtTitle.setText(tituloAntigo)
            edtDescription.setText(descricaoAntiga)

            // 🔥 ESCONDE BOTÃO AO EDITAR
            btnSave.visibility = View.GONE
        } else {
            // 🔥 MOSTRA BOTÃO SOMENTE EM NOVA NOTA
            btnSave.visibility = View.VISIBLE
        }

        // 🎨 CORES
        configurarCores()

        // ✏️ AUTO SAVE ENQUANTO DIGITA
        configurarAutoSave()

        // 💾 BOTÃO SALVAR (SÓ PARA NOVA NOTA)
        btnSave.setOnClickListener {

            val title = edtTitle.text.toString().trim()
            val description = edtDescription.text.toString().trim()

            if (title.isEmpty() && description.isEmpty()) {
                Toast.makeText(this, "Preencha o título ou a anotação para salvar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            salvarNota()
            Toast.makeText(this, "Anotação salva", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        salvarNota()
    }

    // ✏️ AUTO SAVE COM DELAY
    private fun configurarAutoSave() {

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                salvarComDelay()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        edtTitle.addTextChangedListener(textWatcher)
        edtDescription.addTextChangedListener(textWatcher)
    }

    private fun salvarComDelay() {
        runnable?.let { handler.removeCallbacks(it) }

        runnable = Runnable {
            salvarNota()
        }

        handler.postDelayed(runnable!!, 1000)
    }

    // 🎨 CORES
    private fun configurarCores() {

        findViewById<View>(R.id.colorYellow).setOnClickListener {
            corSelecionada = "#FFE7C2"
        }

        findViewById<View>(R.id.colorBlue).setOnClickListener {
            corSelecionada = "#D9F4FF"
        }

        findViewById<View>(R.id.colorPurple).setOnClickListener {
            corSelecionada = "#F6D8FF"
        }

        findViewById<View>(R.id.colorGreen).setOnClickListener {
            corSelecionada = "#DFFFD8"
        }

        findViewById<View>(R.id.colorOrange).setOnClickListener {
            corSelecionada = "#FFF3C7"
        }
    }

    // 💾 SALVAR NOTA
    private fun salvarNota() {

        val title = edtTitle.text.toString().trim()
        val description = edtDescription.text.toString().trim()

        // ❌ não salva vazio
        if (title.isEmpty() && description.isEmpty()) return

        // ❌ não salva se não mudou
        if (title == tituloAntigo && description == descricaoAntiga) return

        if (noteId == 0) {
            db.insertNote(
                Note(
                    titulo = title,
                    descricao = description,
                    cor = corSelecionada
                )
            )
        } else {
            db.updateNote(
                Note(
                    id = noteId,
                    titulo = title,
                    descricao = description,
                    cor = corSelecionada
                )
            )
        }

        // 🔥 atualiza valores antigos
        tituloAntigo = title
        descricaoAntiga = description
    }
}