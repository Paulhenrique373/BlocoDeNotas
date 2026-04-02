package com.example.blocodenotas

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        btnSave = findViewById(R.id.btnSave)

        db = DatabaseHelper(this)

        noteId = intent.getIntExtra("id", 0)

        if (noteId != 0) {
            edtTitle.setText(intent.getStringExtra("title"))
            edtDescription.setText(intent.getStringExtra("description"))
            btnSave.text = "Atualizar"
        }

        btnSave.setOnClickListener {
            val title = edtTitle.text.toString().trim()
            val description = edtDescription.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (noteId == 0) {
                val success = db.insertNote(Note(titulo = title, descricao = description))

                if (success) {
                    Toast.makeText(this, "Anotação salva", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                val success = db.updateNote(Note(noteId, title, description))

                if (success) {
                    Toast.makeText(this, "Anotação atualizada", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}