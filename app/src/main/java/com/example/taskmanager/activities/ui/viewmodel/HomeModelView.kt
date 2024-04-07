package com.example.taskmanager.activities.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.activities.data.model.ChatModel
import com.example.taskmanager.activities.data.model.ChatRepo

class HomeModelView: ViewModel() {
    val chatRepo = ChatRepo()
    fun getChats():LiveData<List<ChatModel>>{
        return chatRepo.getChats()
    }

}