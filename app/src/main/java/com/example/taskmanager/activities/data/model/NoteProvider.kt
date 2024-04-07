package com.example.taskmanager.activities.data.model

import java.sql.Timestamp

class NoteProvider {
    companion object {
        fun random(): NoteModel {
            val posicion:Int = (0..2).random()
            return note[posicion]
        }

        private val note = listOf<NoteModel>(
        NoteModel("1",
            "1",
            "Primera Nota",
            "Esta nota es de prueba FORTNTIE",
            Timestamp(System.currentTimeMillis())
        ),
        NoteModel("2",
            "1",
            "Srgunda nota",
            "A poco si tilin?",
            Timestamp(System.currentTimeMillis())
        ),
        NoteModel("3",
            "1",
            "Tercera nota",
            "A poco no tilin?",
            Timestamp(System.currentTimeMillis())
        ),
    )}

}