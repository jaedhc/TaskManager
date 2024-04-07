package com.example.taskmanager.activities.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.activities.data.model.NoteModel
import com.example.taskmanager.activities.data.model.NoteProvider

class NoteViewModel: ViewModel() {
    val notaModel = MutableLiveData<NoteModel>()

    fun randomNote(){
        val currentNote: NoteModel = NoteProvider.random()
        notaModel.postValue(currentNote)
    }
}