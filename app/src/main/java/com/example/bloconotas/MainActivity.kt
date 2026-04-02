
package com.example.bloconotas

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var btnAdd: Button
    private lateinit var listView: ListView

    private val listaNotas = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)
        btnAdd = findViewById(R.id.btnAdd)
        listView = findViewById(R.id.listView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaNotas)
        listView.adapter = adapter

        btnAdd.setOnClickListener {
            val texto = editText.text.toString()
            if (texto.isNotEmpty()) {
                listaNotas.add(texto)
                adapter.notifyDataSetChanged()
                editText.text.clear()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            editText.setText(listaNotas[position])
            listaNotas.removeAt(position)
            adapter.notifyDataSetChanged()
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            listaNotas.removeAt(position)
            adapter.notifyDataSetChanged()
            true
        }
    }
}
