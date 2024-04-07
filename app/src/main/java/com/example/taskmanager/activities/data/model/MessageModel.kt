package com.example.taskmanager.activities.data.model

import com.google.firebase.Timestamp

data class MessageModel (
    val author:String,
    val content:String,
    val sent_at:Timestamp
)