package com.example.taskmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()
    }




    private fun init(){
        binding.btnGoToLogIn.setOnClickListener {
            callLogIn()
            finish()
        }



    }

    private fun callLogIn(){
        val login = Intent(this, LogInActivity::class.java)
        startActivity(login)
    }

}