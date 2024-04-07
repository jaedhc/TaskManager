package com.example.taskmanager.activities.data.model

import com.google.firebase.Timestamp

data class ChatModel (
    val name:String,
    val img:String,
    val lastMsg:String,
    val sId:String,
    val time:Timestamp
)