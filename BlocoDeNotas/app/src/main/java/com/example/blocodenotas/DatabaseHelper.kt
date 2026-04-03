package com.example.blocodenotas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 3 // 🔥 aumentei de novo

        private const val TABLE_NOTES = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_FIXADA = "fixada"
        private const val COLUMN_COR = "cor" // 🔥 NOVO
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = """
            CREATE TABLE $TABLE_NOTES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_FIXADA INTEGER DEFAULT 0,
                $COLUMN_COR TEXT DEFAULT '#FFE7C2'
            )
        """.trimIndent()

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun insertNote(note: Note): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(COLUMN_TITLE, note.titulo)
        values.put(COLUMN_DESCRIPTION, note.descricao)
        values.put(COLUMN_FIXADA, note.fixada)
        values.put(COLUMN_COR, note.cor) // 🔥 salvar cor

        val result = db.insert(TABLE_NOTES, null, values)
        db.close()

        return result != -1L
    }

    fun getAllNotes(): MutableList<Note> {
        val list = mutableListOf<Note>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NOTES ORDER BY $COLUMN_FIXADA DESC, $COLUMN_ID DESC",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    id = cursor.getInt(0),
                    titulo = cursor.getString(1),
                    descricao = cursor.getString(2),
                    fixada = cursor.getInt(3),
                    cor = cursor.getString(4) // 🔥 pegar cor
                )
                list.add(note)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }

    fun updateNote(note: Note): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(COLUMN_TITLE, note.titulo)
        values.put(COLUMN_DESCRIPTION, note.descricao)
        values.put(COLUMN_FIXADA, note.fixada)
        values.put(COLUMN_COR, note.cor) // 🔥 atualizar cor

        val result = db.update(
            TABLE_NOTES,
            values,
            "$COLUMN_ID=?",
            arrayOf(note.id.toString())
        )

        db.close()
        return result > 0
    }

    fun deleteNote(id: Int): Boolean {
        val db = writableDatabase

        val result = db.delete(
            TABLE_NOTES,
            "$COLUMN_ID=?",
            arrayOf(id.toString())
        )

        db.close()
        return result > 0
    }
}