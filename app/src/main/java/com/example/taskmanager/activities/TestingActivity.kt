package com.example.taskmanager.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityTestingBinding
import com.google.android.gms.auth.api.Auth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class TestingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityTestingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        if(currentUser == null){
            callLogin()
        } else {
            val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("prefUserId", "void")
            //Toast.makeText(this, userId, Toast.LENGTH_LONG).show()
            updateUI(userId.toString())
        }

        binding.btnClose.setOnClickListener {
            closeSession()
        }
    }

    private fun updateUI(userId:String){
        val db = Firebase.firestore
        var j = 0
        db.collection("Users").whereEqualTo("userId", userId).get().addOnSuccessListener { list ->
            if(list.documents.isNotEmpty()){
                list.documents.forEach{ doc ->
                    binding.txtName.text = doc.getString("name")
                    binding.txtEmail.text = doc.getString("email")
                    binding.txtVerified.text = doc.getString("provider")
                    val uri = doc.getString("photoURL")

                    Glide.with(this)
                        .load(uri)
                        .into(binding.imgExample)

                    db.collection("Tasks").whereEqualTo("userId", userId).get().addOnSuccessListener { tlist ->
                        Toast.makeText(this, tlist.size().toString(), Toast.LENGTH_LONG).show()
                        if(tlist.documents.isNotEmpty()){
                            list.documents.forEach{tasks ->
                                j += 1
                                //Toast.makeText(this, j.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun closeSession(){
        val sharedPreferences = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        auth.signOut()
        edit.clear().commit()
        callLogin()
    }

    private fun callLogin(){
        val login = Intent(this, LogInActivity::class.java)
        startActivity(login)
        finish()
    }

}