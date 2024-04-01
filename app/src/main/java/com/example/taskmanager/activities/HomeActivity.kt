package com.example.taskmanager.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.taskmanager.databinding.ActivityHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        auth = Firebase.auth
        val currentUser = auth.currentUser

        if(currentUser == null){
            callLogin()
        } else {
            val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("prefUserId", "void")
            updateUI(userId.toString())
        }

        binding.btnClose.setOnClickListener {
            logOut()
        }
    }

    private fun logOut(){
        val sharedPreferences = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        auth.signOut()
        edit.clear().apply()
        callLogin()
    }

    private fun updateUI(userId:String){
        val db = Firebase.firestore
        db.collection("Users").whereEqualTo("userId", userId).get().addOnSuccessListener { list ->
            if(list.documents.isNotEmpty()){
                list.documents.forEach{ doc ->
                    val uri = doc.getString("photoURL")

                    Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .into(binding.imgUser)

                    val text = doc.getString("name")
                    binding.txtWelcome.text = "Bienvenido $text!"

                    db.collection("Tasks").whereEqualTo("userId", userId).get().addOnSuccessListener { tlist ->
                        if(tlist.documents.isNotEmpty()){
                            list.documents.forEach{tasks ->
                                //Toast.makeText(this, j.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun callLogin(){
        val login = Intent(this, LogInActivity::class.java)
        startActivity(login)
        finish()
    }
}