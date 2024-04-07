package com.example.taskmanager.activities.ui.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.taskmanager.R
import com.example.taskmanager.activities.adapter.ChatAdapter
import com.example.taskmanager.activities.casos_uso.casosUsoAtivities
import com.example.taskmanager.activities.data.model.ChatModel
import com.example.taskmanager.activities.data.model.UsersModel
import com.example.taskmanager.activities.ui.viewmodel.HomeModelView
import com.example.taskmanager.databinding.ActivityHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatViewModel: HomeModelView
    private val casoActivities by lazy { casosUsoAtivities(this) }
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
                updateUI(userId.toString(), image!!, name!!, role!!)
                initCallService(userId.toString(), name)
            }
        }

        binding.btnUsers.setOnClickListener {
            val sharedPreferences = applicationContext.getSharedPreferences("admin", Context.MODE_PRIVATE)
            val edit = sharedPreferences.edit()

            edit.putString("ventana", "user").apply()
            casoActivities.callTesting()
        }

        binding.userInfo.setOnClickListener{
            finish()
            casoActivities.callSettings()
        }
    }

    private fun initCallService(userId: String, userName: String){
        val appID:Long = 1132129954
        val appSign = "d00c8218b989f84d2773103ed582fbb13ccfe3dda25f888b60891f97109df24b"
        val aplication = application
        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
        //callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true
        val notificationConfig = ZegoNotificationConfig()
        notificationConfig.sound = "zego_uikit_sound_call"
        notificationConfig.channelID = "CallInvitation"
        notificationConfig.channelName = "CallInvitation"

        Log.d("primer_ID", userId)

        ZegoUIKitPrebuiltCallService.init(
            aplication, appID, appSign, userId, userName, callInvitationConfig
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        ZegoUIKitPrebuiltCallService.unInit()
    }

    private fun updateUI(userId:String, uri:String, userName:String, role:String){
        val db = Firebase.firestore

        Glide.with(this)
            .load(uri)
            .circleCrop()
            .into(binding.imgUser)

        binding.txtWelcome.text = "${getString(R.string.text_welcome)} $userName!"

        chatViewModel = ViewModelProvider(this).get(HomeModelView::class.java)

        chatAdapter = ChatAdapter()

        chatViewModel.getChats().observe(this, Observer {
            chatAdapter.setChatList(it)

            binding.chatsRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = chatAdapter
            }
        })

        chatAdapter.setOnChatClickListener(object :ChatAdapter.OnChatClickListener{
            override fun onChatSelected(position: Int, chat: ChatModel) {
                val firestore = FirebaseFirestore.getInstance()
                Log.d("fort", chat.toString())
                firestore.collection("Users").document(chat.sId).get().addOnCompleteListener {
                    val id = it.result.get("userId").toString()
                    val status = it.result.get("status").toString()
                    val img = it.result.get("photoURL").toString()
                    val name = it.result.get("name").toString()
                    val role = it.result.get("role").toString()

                    val user = UsersModel(id,status,img,name, role)

                    val sharedPreferences = getSharedPreferences("chat", Context.MODE_PRIVATE)
                    val edit = sharedPreferences.edit()

                    val gson = Gson()
                    val json = gson.toJson(user)

                    edit.putString("chat_info", json)
                    edit.apply()

                    casoActivities.callChat()
                }
            }

        })

    }

}