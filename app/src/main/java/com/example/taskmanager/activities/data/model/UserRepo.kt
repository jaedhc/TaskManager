package com.example.taskmanager.activities.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanager.activities.Utils
import com.google.firebase.firestore.FirebaseFirestore

class UserRepo {

    private val firestore = FirebaseFirestore.getInstance()

    fun getUsers():LiveData<List<UsersModel>>{

        val users = MutableLiveData<List<UsersModel>>()

        firestore.collection("Users").addSnapshotListener{ snapshot, exception ->

            val usersList = mutableListOf<UsersModel>()
            snapshot?.documents?.forEach{doc ->
                val name = doc.getString("name")
                val status = doc.getString("status")
                val img = doc.getString("photoURL")
                val role = doc.getString("role")
                val user = UsersModel(doc.id, status, img, name, role)

                if(doc.id != Utils.getUidLoggedIn()){
                    user.let {
                        usersList.add(it)
                    }
                }
                users.value = usersList
            }
        }

        return users
    }

}