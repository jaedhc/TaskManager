package com.example.taskmanager.activities.data.model
import  java.sql.Timestamp

data class NoteModel(
    val noteId:String = "",
    val userId:String = "",
    val title:String="",
    val description:String="",
    val createdAt: Timestamp
    )