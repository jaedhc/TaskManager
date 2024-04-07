package com.example.taskmanager.activities.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.activities.data.model.UserRepo
import com.example.taskmanager.activities.data.model.UsersModel

class ListUsersViewModel: ViewModel() {
    val name: MutableLiveData<String> = MutableLiveData<String>()
    val iamgeUrl = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    val usersRepo = UserRepo()

    fun getUsers(): LiveData<List<UsersModel>>{
        return usersRepo.getUsers()
    }


}