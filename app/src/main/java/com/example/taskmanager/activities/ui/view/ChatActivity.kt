package com.example.taskmanager.activities.ui.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.taskmanager.activities.adapter.MessageAdapter
import com.example.taskmanager.activities.data.model.UsersModel
import com.example.taskmanager.activities.ui.viewmodel.ChatViewModel
import com.example.taskmanager.databinding.ActivityChatBinding
import com.google.gson.Gson
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var chatViewModel: ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("chat", Context.MODE_PRIVATE)
        val storedJson = sharedPreferences.getString("chat_info", null)

        if(storedJson != null){
            val chatUser = Gson().fromJson(storedJson, UsersModel::class.java)
            val userId = chatUser.userId
            val name = chatUser.userName
            val image = chatUser.imageUrl
            updateUI(userId!!, name!!, image!!)
        }

        binding.toolbar.toolbarBack.setOnClickListener {
            finish()
        }
    }

    private fun updateUI(id:String, name:String, image:String){
        Glide.with(this)
            .load(image)
            .circleCrop()
            .into(binding.toolbar.chatImg)

        binding.toolbar.toolbarName.text = name

        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        messageAdapter = MessageAdapter()

        chatViewModel.getMessages(id).observe(this, Observer {
            messageAdapter.setMessageList(it)
            binding.messagesRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = messageAdapter
            }
        })

        binding.btnSend.setOnClickListener {
            val message = binding.edtMessage.text.toString()
            chatViewModel.sendMessage(message, id)
            binding.edtMessage.setText("")
        }

        startVideoCall(id,name)
        startVoiceCall(id,name)
    }

    private fun startVideoCall(reciverId:String, reciverName:String){

        binding.toolbar.videoCallBtn.setIsVideoCall(true)
        binding.toolbar.videoCallBtn.resourceID = "zego_uikit_call"
        binding.toolbar.videoCallBtn.setInvitees(listOf(ZegoUIKitUser(reciverId, reciverName)))
    }

    private fun startVoiceCall(reciverId:String, reciverName:String){

        binding.toolbar.voiceCallBtn.setIsVideoCall(false)
        binding.toolbar.voiceCallBtn.resourceID = "zego_uikit_call"
        binding.toolbar.voiceCallBtn.setInvitees(listOf(ZegoUIKitUser(reciverId, reciverName)))
    }

}