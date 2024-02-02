package com.example.taskmanager.activities

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityLogInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.util.UUID

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){

        binding.prograssBarLogin.visibility = View.INVISIBLE

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        val currentUser = auth.currentUser

        if(currentUser != null){
            callTest()
        }

        binding.btnLogin.setOnClickListener {
            val userEmail = binding.textEmail.text.toString()
            val userPass = binding.textPassword.text.toString()

            verifyUserInfo(userEmail, userPass)
        }

        binding.btnSignin.setOnClickListener {
            callSignIn()
        }

        binding.textForgot.setOnClickListener {
            val userEmail = binding.textEmail.text.toString()
            sendPassRecovery(userEmail)
        }

        binding.btnGoogle.setOnClickListener {
            loginGoogle()
        }
    }

    private fun sendPassRecovery(email:String){
        if (checkEmail(email)){
            return
        } else {
            auth = Firebase.auth

            auth.sendPasswordResetEmail(email).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Toast.makeText(this,"Correo enviado con éxito", Toast.LENGTH_LONG).show()
                    binding.boxEmail.error = null
                } else {
                    if(task.exception is FirebaseAuthInvalidUserException){
                        binding.boxEmail.error = getString(R.string.text_error_nouser)
                    }
                }
            }
        }
    }

    private fun checkEmail(email:String):Boolean{
        val pattern = Patterns.EMAIL_ADDRESS

        if (email.isEmpty()){
            binding.boxEmail.error = getString(R.string.text_error_empty)
            return true
        }

        if(!pattern.matcher(email).matches()){
            binding.boxEmail.error = getString(R.string.text_error_email)
            return true
        }

        return false
    }

    private fun verifyUserInfo(email:String, pass:String){
        var error = checkEmail(email)

        if (pass.isEmpty()){
            binding.boxPassword.error = getString(R.string.text_error_empty)
            error = true
        }

        if(!error){
            binding.boxEmail.error = null
            binding.boxPassword.error = null

            logIn(email, pass)
        }
    }

    private fun logIn(email: String, pass: String){
        binding.prograssBarLogin.visibility = View.VISIBLE
        val sharedPreferences = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()

        auth = Firebase.auth


        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                edit.putString("prefEmail", email).commit()
                val db = Firebase.firestore

                val path2 = db.collection("Tasks").whereEqualTo("userId", email).get().addOnCompleteListener {

                }

                val path = db.collection("Users")
                path.whereEqualTo("email", email).get().addOnSuccessListener { docs ->
                    docs.forEach{ doc ->
                        val name = doc.getString("name")
                        val uniqueID = doc.getString("userId")
                        edit.putString("prefName", name).commit()
                        edit.putString("prefUserId", uniqueID).commit()
                        edit.apply()
                        binding.prograssBarLogin.visibility = View.INVISIBLE
                        callTest()
                    }
                }
            } else {

                when (task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        binding.boxPassword.error = getString(R.string.text_error_credentials)
                        binding.boxEmail.error = getString(R.string.text_error_credentials)
                    }

                    is FirebaseAuthInvalidUserException -> {
                        binding.boxPassword.error = getString(R.string.text_error_credentials)
                        binding.boxEmail.error = getString(R.string.text_error_credentials)
                    }

                    is FirebaseTooManyRequestsException -> {
                        Toast.makeText(
                            this,
                            getString(R.string.text_error_toomany),
                            Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> {
                        Toast.makeText(this, "${task.exception}", Toast.LENGTH_LONG).show()
                    }
                }

                binding.prograssBarLogin.visibility = View.INVISIBLE
            }
        }
    }

    private fun loginGoogle(){
        val signIntent = googleSignInClient.signInIntent
        launcher.launch(signIntent)

        binding.prograssBarLogin.visibility = View.VISIBLE
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            manageResule(task)
        } else {
            binding.textView.text = result.toString()
            print(result.toString())
            //Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun manageResule(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account:GoogleSignInAccount? = task.result
            if(account!=null){
                updateUi(account)
            }
        }else{
            Toast.makeText(this, "ERROR2222222222", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUi(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                verifyUser()
            } else {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verifyUser(){
        val user = Firebase.auth.currentUser
        user?.let {
            val name = it.displayName
            val email = it.email
            val provider = getString(R.string.google_provider)
            val photo = it.photoUrl

            val sharedPreferences = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
            val edit = sharedPreferences.edit()

            val uniqueID = auth.uid.toString()

            edit.putString("prefEmail", email).commit()
            edit.putString("prefName", name).commit()
            edit.putString("prefProvider", provider).commit()
            edit.putString("prefUserId", uniqueID).commit()
            edit.apply()

            val db = Firebase.firestore

            val userData = hashMapOf(
                "userId" to uniqueID,
                "name" to name,
                "email" to email,
                "provider" to getString(R.string.google_provider),
                "photoURL" to photo
            )

            db.collection("Users").whereEqualTo("userId", uniqueID).get().addOnSuccessListener {
                if(it.documents.isNotEmpty()){
                    callTest()
                } else {
                    db.collection("Users").document(uniqueID).set(userData).addOnCompleteListener { query ->
                        if(query.isSuccessful){
                            callTest()
                        }
                    }
                }
            }

            binding.prograssBarLogin.visibility = View.INVISIBLE
        }
    }

    private fun callSignIn(){
        val login = Intent(this, SignUpActivity::class.java)
        startActivity(login)
    }

    private fun callTest(){
        val test = Intent(this, TestingActivity::class.java)
        startActivity(test)
        finish()
    }

}