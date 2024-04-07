package com.example.taskmanager.activities.data.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanager.activities.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepo {

    private val firestore = FirebaseFirestore.getInstance()

    fun getChats():LiveData<List<ChatModel>>{

        val chML = MutableLiveData<List<ChatModel>>()

        val usr = Utils.getUidLoggedIn()
        firestore.collection("Users").document(usr).addSnapshotListener{ snap, listener ->
            if (snap != null) {
                val chats = snap.get("chatRooms") as List<*>
                val chtList = mutableListOf<ChatModel>()
                for (chat in chats){
                    firestore.collection("ChatRooms").document(chat.toString()).get().addOnCompleteListener { room ->
                        var members = room.result["members"] as List<*>
                        var sId = ""
                        for (member in members){
                            if(member != usr){
                                sId = member.toString()
                            }
                        }
                        var msgs = room.result["messages"] as List<*>
                        val lastMsg = msgs.last() as Map<*, *>

                        firestore.collection("Users").document(sId).get().addOnSuccessListener { sUsr ->
                            var img = sUsr.get("photoURL").toString()
                            var name = sUsr.getString("name").toString()

                            val objChat = ChatModel(name, img, lastMsg["content"].toString(), sId, lastMsg["sent_at"] as Timestamp)

                            Log.d("Si", objChat.toString())

                            objChat.let {
                                chtList.add(it)
                            }

                            chML.value = chtList
                        }
                    }
                }
            }
        }
        /*firestore.collection("Users").document(usr).get().addOnCompleteListener {
            val chats = it.result["chatRooms"] as List<*>

        }*/

        return chML
    }
}