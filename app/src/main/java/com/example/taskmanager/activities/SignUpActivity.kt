package com.example.taskmanager.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()
    }

    private fun init(){
        binding.prograssBarSignup.visibility = View.INVISIBLE

        binding.btnGoToLogIn.setOnClickListener {
            callLogIn()
        }

        binding.btnSignin.setOnClickListener {
            val userName = binding.textUsername.text.toString()
            val userEmail = binding.textEmail.text.toString()
            val userPassword = binding.textPassword.text.toString()

            verifyUserInfo(userName, userEmail, userPassword)
        }
    }

    private fun verifyUserInfo(name:String, email:String, pass:String){

        val pattern = Patterns.EMAIL_ADDRESS
        var error = false

        if (name.isEmpty()){
            binding.boxUsername.error = getString(R.string.text_error_empty)
            error = true
        }

        if (email.isEmpty()){
            binding.boxEmail.error = getString(R.string.text_error_empty)
            error = true
        }

        if (pass.isEmpty()){
            binding.boxPassword.error = getString(R.string.text_error_empty)
            error = true
        }

        if(!pattern.matcher(email).matches()){
            binding.boxEmail.error = getString(R.string.text_error_email)
            error = true
        }

        if(!error){
            binding.boxUsername.error = null
            binding.boxEmail.error = null
            binding.boxPassword.error = null

            signUpUser(name, email, pass)
        }

    }

    private fun signUpUser(name:String, email:String, pass:String){
        binding.prograssBarSignup.visibility = View.VISIBLE

        auth = Firebase.auth
        val db = Firebase.firestore

        val uniqueID = UUID.randomUUID().toString()
        val user = hashMapOf(
            "userId" to uniqueID,
            "name" to name,
            "email" to email,
            "provider" to getString(R.string.email_provider),
            "photoURL" to getString(R.string.default_prof_pic)
        )

        val sharedPreferences = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)

        //Creación de una nueva cuenta mediante firebase email auth
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener{ task ->
            if(task.isSuccessful){
                //Agregar los datos del usuario a la base de datos de firestore
                db.collection("Users")
                    .document(uniqueID).set(user)
                    .addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_LONG).show()
                        binding.prograssBarSignup.visibility = View.INVISIBLE
                        //Mandar el id del usuario a la página principal
                        sharedPreferences.edit().putString("prefUserId", uniqueID).apply()
                        callTest()
                    } else {
                        Toast.makeText(this, "${task.exception}", Toast.LENGTH_LONG).show()
                        binding.prograssBarSignup.visibility = View.INVISIBLE
                    }
                }
            } else {
                if(task.exception is FirebaseAuthUserCollisionException){
                    binding.boxEmail.error = getString(R.string.text_error_duplicate)
                    binding.prograssBarSignup.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun callLogIn(){
        finish()
    }

    private fun callTest(){
        val test = Intent(this, TestingActivity::class.java)
        startActivity(test)
        finish()
    }

}