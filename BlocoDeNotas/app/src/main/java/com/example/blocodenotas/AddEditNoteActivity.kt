package com.example.blocodenotas

import android.os.Bundle
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
    private var jaSalvou = false

    // 🔥 guardar valores antigos
    private var tituloAntigo = ""
    private var descricaoAntiga = ""

    // 🎨 cor da nota
    private var corSelecionada = "#FFE7C2"

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
            corSelecionada = intent.getStringExtra("cor") ?: "#FFE7C2" // 🔥 pega cor antiga

            edtTitle.setText(tituloAntigo)
            edtDescription.setText(descricaoAntiga)
            btnSave.text = "Atualizar"
        }

        // 🎨 CORES
        configurarCores()

        btnSave.setOnClickListener {
            salvarNota(true)
        }
    }

    override fun onPause() {
        super.onPause()
        salvarNota(false)
    }

    // 🎨 FUNÇÃO PRA ORGANIZAR CORES
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
    private fun salvarNota(mostrarToast: Boolean) {

        if (jaSalvou) return

        val title = edtTitle.text.toString().trim()
        val description = edtDescription.text.toString().trim()

        // ❌ não salva vazio
        if (title.isEmpty() && description.isEmpty()) return

        // ❌ não salva se não mudou
        if (title == tituloAntigo && description == descricaoAntiga) return

        if (noteId == 0) {
            // 🔥 NOVA NOTA
            val success = db.insertNote(
                Note(
                    titulo = title,
                    descricao = description,
                    cor = corSelecionada
                )
            )

            if (success) {
                jaSalvou = true
                if (mostrarToast) {
                    Toast.makeText(this, "Anotação salva", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        } else {
            // 🔥 ATUALIZAR NOTA
            val success = db.updateNote(
                Note(
                    id = noteId,
                    titulo = title,
                    descricao = description,
                    cor = corSelecionada
                )
            )

            if (success) {
                jaSalvou = true
                if (mostrarToast) {
                    Toast.makeText(this, "Anotação atualizada", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}