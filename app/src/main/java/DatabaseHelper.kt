package com.example.app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Classe que gerencia o banco de dados
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Chamado quando o banco de dados é criado pela primeira vez
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COL_DISCIPLINA TEXT, "
                + "$COL_CONTEUDO TEXT, "
                + "$COL_DIA TEXT, "
                + "$COL_HORARIO TEXT, "
                + "$COL_REALIZADO INTEGER DEFAULT 0)")
        db.execSQL(createTable)
    }

    // Chamado quando o banco de dados precisa ser atualizado
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        // Definições do banco de dados
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Estudos.db"
        const val TABLE_NAME = "estudos"
        const val COL_ID = "id"
        const val COL_DISCIPLINA = "disciplina"
        const val COL_CONTEUDO = "conteudo"
        const val COL_DIA = "dia"
        const val COL_HORARIO = "horario"
        const val COL_REALIZADO = "realizado"
    }
}
