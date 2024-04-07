package com.example.taskmanager.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.example.taskmanager.activities.casos_uso.casosUsoAtivities
import com.example.taskmanager.activities.data.model.UsersModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date

class Utils {

    companion object{
        /*@SuppressLint("StaticFieldLeak")
        val context = MyApplication.instance.applicationContext*/
        @SuppressLint("StaticFieldLeak")
        val firestore = FirebaseFirestore.getInstance()


        private val auth = FirebaseAuth.getInstance()
        private var userid: String = ""

        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_IMAGE_PICK = 2
        const val MESSAGE_RIGHT = 1
        const val MESSAGE_LEFT = 2
        const val CHANNEL_ID = "com.example.chatmessenger"

        fun getUidLoggedIn():String{
            if(auth.currentUser != null){
                userid = auth.currentUser!!.uid
            }

            return userid
        }

        fun logOut(){
            auth.signOut()
        }

        fun setUserInfo(context: Context, activity: Activity){

            val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
            val casosUsoAtivities by lazy { casosUsoAtivities(activity) }
            val edit = sharedPreferences.edit()
            lateinit var user:UsersModel

            val db = Firebase.firestore
            db.collection("Users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
                user = UsersModel(it.id,
                    it.getString("status"),
                    it.getString("photoURL"),
                    it.getString("name"),
                    it.getString("role"))


                val gson = Gson()
                val json = gson.toJson(user)

                edit.putString("user_info", json)
                edit.apply()

                activity.finishAffinity()
                casosUsoAtivities.callHome()

            }


        }

        fun getTime(): String {
            val formatter = SimpleDateFormat("HH:mm:ss")
            val date: Date = Date(System.currentTimeMillis())
            val stringdate = formatter.format(date)


            return stringdate

        }

    }
}