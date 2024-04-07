package com.example.taskmanager.activities.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanager.activities.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class MessagesRepo {

    private val firestore = FirebaseFirestore.getInstance()
    fun getMessages(sUserId:String):LiveData<List<MessageModel>>{

        val messages = MutableLiveData<List<MessageModel>>()
        firestore.collection("ChatRooms")
            .whereArrayContains("members", Utils.getUidLoggedIn())
            .addSnapshotListener{ snap, _ ->

            val messageList = mutableListOf<MessageModel>()

            snap?.documents?.forEach{ doc ->
                val members = doc["members"] as List<*>
                if(members.contains(sUserId)){
                    val array = doc["messages"] as List<*>
                    for (item in array) {
                        val hash = item as Map<*, *>

                        val author = hash["author"].toString()
                        val content = hash["content"].toString()
                        val sent_at = hash["sent_at"] as Timestamp

                        val message = MessageModel(author, content, sent_at)

                        message.let {
                            messageList.add(message)
                        }

                        messages.value = messageList
                    }
                }
            }

        }
        return messages
    }


}