package com.example.taskmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.databinding.ActivityLogInBinding

private lateinit var binding: ActivityLogInBinding

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        binding.btnSignin.setOnClickListener {
            callSignIn()
            finish()
        }
    }

    private fun callSignIn(){
        val login = Intent(this, SignUpActivity::class.java)
        startActivity(login)
    }
}