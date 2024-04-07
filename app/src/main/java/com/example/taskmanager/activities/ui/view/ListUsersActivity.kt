package com.example.taskmanager.activities.ui.view

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.activities.adapter.UserAdapter
import com.example.taskmanager.activities.casos_uso.casosUsoAtivities
import com.example.taskmanager.activities.data.model.UsersModel
import com.example.taskmanager.activities.ui.viewmodel.ListUsersViewModel
import com.example.taskmanager.databinding.ActivityListUsersBinding
import com.google.gson.Gson

class ListUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListUsersBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: ListUsersViewModel
    private val casosUsoAtivities by lazy { casosUsoAtivities(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(ListUsersViewModel::class.java)

        userAdapter = UserAdapter()

        userViewModel.getUsers().observe(this, Observer {
            userAdapter.setUserList(it)
            //Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            binding.usersRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = userAdapter
            }
        })

        val sharedPreferences = getSharedPreferences("admin", Context.MODE_PRIVATE)
        val ventana = sharedPreferences.getString("ventana", "otra")

        val con = this
        userAdapter.setOnUserClickListener(object : UserAdapter.OnUserClickListener{
            override fun onUserSelected(position: Int, users: UsersModel) {

                if(ventana == "admin"){
                    Toast.makeText(con, "Hola", Toast.LENGTH_LONG).show()
                } else {
                    val sharedPreferences = getSharedPreferences("chat", Context.MODE_PRIVATE)
                    val edit = sharedPreferences.edit()

                    val gson = Gson()
                    val json = gson.toJson(users)

                    edit.putString("chat_info", json)
                    edit.apply()

                    casosUsoAtivities.callChat()
                }
            }

        })

        binding.toolbar.toolbarBack.setOnClickListener {
            finish()
        }

    }
}