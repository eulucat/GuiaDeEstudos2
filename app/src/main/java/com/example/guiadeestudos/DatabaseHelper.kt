package com.example.guiadeestudos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "estudos.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "estudos"
        const val COLUMN_ID = "id"
        const val COLUMN_DISCIPLINA = "disciplina"
        const val COLUMN_CONTEUDO = "conteudo"
        const val COLUMN_DIA = "dia"
        const val COLUMN_HORARIO = "horario"
        const val COLUMN_REALIZADO = "realizado"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DISCIPLINA TEXT,
                $COLUMN_CONTEUDO TEXT,
                $COLUMN_DIA TEXT,
                $COLUMN_HORARIO TEXT,
                $COLUMN_REALIZADO INTEGER DEFAULT 0)
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun adicionarEstudo(disciplina: String, conteudo: String, dia: String, horario: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DISCIPLINA, disciplina)
            put(COLUMN_CONTEUDO, conteudo)
            put(COLUMN_DIA, dia)
            put(COLUMN_HORARIO, horario)
            put(COLUMN_REALIZADO, 0)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun obterEstudosNaoRealizados(): List<Estudo> {
        val estudosNaoRealizados = mutableListOf<Estudo>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_REALIZADO = 0", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val disciplina = cursor.getString(cursor.getColumnIndex(COLUMN_DISCIPLINA))
                val conteudo = cursor.getString(cursor.getColumnIndex(COLUMN_CONTEUDO))
                val dia = cursor.getString(cursor.getColumnIndex(COLUMN_DIA))
                val horario = cursor.getString(cursor.getColumnIndex(COLUMN_HORARIO))
                estudosNaoRealizados.add(Estudo(id, disciplina, conteudo, dia, horario, false))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return estudosNaoRealizados
    }

    fun marcarEstudoComoRealizado(id: Long) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_REALIZADO, 1)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun obterEstudosRealizados(): List<Estudo> {
        val estudosRealizados = mutableListOf<Estudo>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_REALIZADO = 1", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val disciplina = cursor.getString(cursor.getColumnIndex(COLUMN_DISCIPLINA))
                val conteudo = cursor.getString(cursor.getColumnIndex(COLUMN_CONTEUDO))
                val dia = cursor.getString(cursor.getColumnIndex(COLUMN_DIA))
                val horario = cursor.getString(cursor.getColumnIndex(COLUMN_HORARIO))
                estudosRealizados.add(Estudo(id, disciplina, conteudo, dia, horario, true))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return estudosRealizados
    }
    fun excluirEstudo(id: Long): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete("estudos", "id = ?", arrayOf(id.toString()))
        db.close()
        return rowsDeleted
    }

    fun marcarEstudoComoNaoRealizado(id: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("realizado", 0) // 0 para não realizado
        }
        db.update("estudos", values, "id = ?", arrayOf(id.toString()))
        db.close()
    }



    data class Estudo(
        val id: Long,
        val disciplina: String,
        val conteudo: String,
        val dia: String,
        val horario: String,
        val realizado: Boolean
    )
}
