package com.example.taskmanager.activities.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.taskmanager.R
import com.example.taskmanager.activities.Utils
import com.example.taskmanager.activities.casos_uso.casosUsoAtivities
import com.example.taskmanager.activities.data.model.UsersModel
import com.example.taskmanager.databinding.ActivityUserSettingsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.gson.Gson
import java.util.Locale




class UserSettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityUserSettingsBinding
    private val casoActivities by lazy { casosUsoAtivities(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)
        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("FORT", "BACK PRESSED")
                finishAffinity()
                casoActivities.callHome()
                // Code that you need to execute on back press, e.g. finish()
            }
        })
    }

    private fun init(){

        auth = Firebase.auth
        val currentUser = auth.currentUser

        if(currentUser == null){
            casoActivities.callLogin()
        } else {

            val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
            val storedJson = sharedPreferences.getString("user_info", null)

            if(storedJson != null){
                val user = Gson().fromJson(storedJson, UsersModel::class.java)
                val userId = user.userId
                val name = user.userName
                val image = user.imageUrl
                val role = user.role
                updateUI(name!!, image!!, role!!)
            }
        }

        binding.btnClose.setOnClickListener {
            logOut()
        }

        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    1 -> {
                        val locale = Locale("es")
                        val config = Configuration(resources.configuration)
                        config.setLocale(locale)
                        resources.updateConfiguration(config, baseContext.resources.displayMetrics)
                        finish()
                        startActivity(intent)

                    }
                    2 -> {
                        val locale = Locale("en")
                        val config = Configuration(resources.configuration)
                        config.setLocale(locale)
                        resources.updateConfiguration(config, baseContext.resources.displayMetrics)
                        finish()
                        startActivity(intent)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateUI(name:String, img:String, role:String){
        Glide.with(this)
            .load(img)
            .circleCrop()
            .into(binding.userSettingsImg)

        binding.userSettingsName.text = name

        if(role != "user"){
            binding.btnAdmin.visibility = View.VISIBLE
        }

        binding.btnAdmin.setOnClickListener {
            val sharedPreferences = applicationContext.getSharedPreferences("admin", Context.MODE_PRIVATE)
            val edit = sharedPreferences.edit()

            edit.putString("ventana", "admin").apply()

            casoActivities.callTesting()
        }

    }
    private fun logOut(){
        var sharedPreferences = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        var edit = sharedPreferences.edit()
        Utils.logOut()
        edit.clear().apply()
        sharedPreferences = applicationContext.getSharedPreferences("chat", Context.MODE_PRIVATE)
        edit = sharedPreferences.edit()
        edit.clear().apply()
        finish()
        casoActivities.callLogin()
    }


}