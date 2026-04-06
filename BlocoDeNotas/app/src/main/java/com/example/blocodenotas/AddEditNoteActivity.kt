package com.example.blocodenotas

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnSave: Button

    private lateinit var btnBold: TextView
    private lateinit var btnItalic: TextView
    private lateinit var btnUnderline: TextView

    private lateinit var db: DatabaseHelper
    private var noteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        btnSave = findViewById(R.id.btnSave)

        btnBold = findViewById(R.id.btnBold)
        btnItalic = findViewById(R.id.btnItalic)
        btnUnderline = findViewById(R.id.btnUnderline)

        db = DatabaseHelper(this)

        // 🔥 RECEBER DADOS (EDIÇÃO)
        noteId = intent.getIntExtra("id", 0)
        edtTitle.setText(intent.getStringExtra("title") ?: "")
        edtDescription.setText(intent.getStringExtra("description") ?: "")

        // 🔥 NEGRITO
        btnBold.setOnClickListener {
            aplicarEstilo(Typeface.BOLD)
        }

        // 🔥 ITÁLICO
        btnItalic.setOnClickListener {
            aplicarEstilo(Typeface.ITALIC)
        }

        // 🔥 SUBLINHADO
        btnUnderline.setOnClickListener {
            aplicarSublinhado()
        }

        // 🎨 COR (segurar texto)
        edtDescription.setOnLongClickListener {
            aplicarCor(Color.RED)
            true
        }

        // 💾 SALVAR (CORRIGIDO)
        btnSave.setOnClickListener {
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()

            // ✅ NOVA REGRA (CORRIGIDA)
            if (title.isEmpty() && desc.isEmpty()) {
                Toast.makeText(this, "Digite algo para salvar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (noteId == 0) {
                db.insertNote(Note(titulo = title, descricao = desc))
            } else {
                db.updateNote(Note(id = noteId, titulo = title, descricao = desc))
            }

            Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // 🔥 NEGRITO / ITÁLICO
    private fun aplicarEstilo(style: Int) {
        val start = edtDescription.selectionStart
        val end = edtDescription.selectionEnd

        if (start < end) {
            val spannable = SpannableStringBuilder(edtDescription.text)
            spannable.setSpan(
                StyleSpan(style),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            edtDescription.text = spannable
            edtDescription.setSelection(end)
        } else {
            Toast.makeText(this, "Selecione o texto primeiro", Toast.LENGTH_SHORT).show()
        }
    }

    // 🔥 SUBLINHADO
    private fun aplicarSublinhado() {
        val start = edtDescription.selectionStart
        val end = edtDescription.selectionEnd

        if (start < end) {
            val spannable = SpannableStringBuilder(edtDescription.text)
            spannable.setSpan(
                UnderlineSpan(),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            edtDescription.text = spannable
            edtDescription.setSelection(end)
        } else {
            Toast.makeText(this, "Selecione o texto primeiro", Toast.LENGTH_SHORT).show()
        }
    }

    // 🎨 COR
    private fun aplicarCor(cor: Int) {
        val start = edtDescription.selectionStart
        val end = edtDescription.selectionEnd

        if (start < end) {
            val spannable = SpannableStringBuilder(edtDescription.text)
            spannable.setSpan(
                ForegroundColorSpan(cor),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            edtDescription.text = spannable
            edtDescription.setSelection(end)
        } else {
            Toast.makeText(this, "Selecione o texto primeiro", Toast.LENGTH_SHORT).show()
        }
    }
}