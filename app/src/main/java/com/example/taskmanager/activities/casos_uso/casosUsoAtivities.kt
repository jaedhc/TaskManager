package com.example.taskmanager.activities.casos_uso

import android.app.Activity
import android.content.Intent
import com.example.taskmanager.activities.ui.view.ChatActivity
import com.example.taskmanager.activities.ui.view.HomeActivity
import com.example.taskmanager.activities.ui.view.ListUsersActivity
import com.example.taskmanager.activities.ui.view.LogInActivity
import com.example.taskmanager.activities.ui.view.UserSettingsActivity

class casosUsoAtivities (val activity: Activity) {

    fun callLogin(){
        val login = Intent(activity, LogInActivity::class.java)
        activity.startActivity(login)
    }

    fun callTesting(){
        val test = Intent(activity, ListUsersActivity::class.java)
        activity.startActivity(test)
    }

    fun callHome(){
        val home = Intent(activity, HomeActivity::class.java)
        activity.startActivity(home)
    }

    fun callSettings(){
        val settings = Intent(activity, UserSettingsActivity::class.java)
        activity.startActivity(settings)
    }

    fun callChat(){
        val chat = Intent(activity, ChatActivity::class.java)
        activity.startActivity(chat)
    }

}