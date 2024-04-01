package com.example.taskmanager.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
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
            finish()
        }

        binding.btnSignin.setOnClickListener {
            val userName = binding.textUsername.text.toString()
            val userEmail = binding.textEmail.text.toString()
            val userPassword = binding.textPassword.text.toString()
            val userRepeat = binding.textRepeat.text.toString()

            //binding.textUsername.doAfterTextChanged { }

            verifyUserInfo(userName, userEmail, userPassword, userRepeat)
        }
    }

    private fun verifyUserInfo(name:String, email:String, pass:String, passRepeat:String){

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

        if(pass != passRepeat){
            binding.boxPassword.error = getString(R.string.text_error_dontmatches)
            binding.boxRepeat.error = getString(R.string.text_error_dontmatches)
            error = true
        }

        if(!error){
            binding.boxUsername.error = null
            binding.boxEmail.error = null
            binding.boxPassword.error = null
            binding.boxRepeat.error = null

            signUpUser(name, email, pass)
        }

    }

    private fun signUpUser(name:String, email:String, pass:String){
        //binding.prograssBarSignup.visibility = View.VISIBLE

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
                                Firebase.auth.currentUser?.sendEmailVerification()
                                Toast.makeText(this, "Verifique su correo electrónico", Toast.LENGTH_LONG).show()
                                binding.prograssBarSignup.visibility = View.INVISIBLE
                                //callLogin()
                            } else {
                                Toast.makeText(this, "${task.exception}", Toast.LENGTH_LONG).show()
                                binding.prograssBarSignup.visibility = View.INVISIBLE
                            }
                        }
                }
                binding.prograssBarSignup.visibility = View.INVISIBLE
            }
    }

    private fun callLogin(){
        val login = Intent(this, LogInActivity::class.java)
        startActivity(login)
        finish()
    }

    private fun callTest(){
        val test = Intent(this, HomeActivity::class.java)
        startActivity(test)
        finish()
    }

}