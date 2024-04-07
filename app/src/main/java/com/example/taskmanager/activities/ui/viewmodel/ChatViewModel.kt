package com.example.taskmanager.activities.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.activities.Utils
import com.example.taskmanager.activities.data.model.MessageModel
import com.example.taskmanager.activities.data.model.MessagesRepo
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ChatViewModel: ViewModel() {

    val messageRepo = MessagesRepo()
    private val firestore = FirebaseFirestore.getInstance()
    fun getMessages(id:String):LiveData<List<MessageModel>>{
        return messageRepo.getMessages(id)
    }

    fun sendMessage(content:String, sId:String){
        val message = hashMapOf(
            "author" to Utils.getUidLoggedIn(),
            "content" to content,
            "sent_at" to Timestamp.now()
        )

        firestore.collection("ChatRooms").whereArrayContains("members", sId).get().addOnCompleteListener {
            if (it.isSuccessful){
                if(it.result.documents.isEmpty()){
                    val members = listOf(sId, Utils.getUidLoggedIn())
                    val id = UUID.randomUUID().toString()
                    val room = hashMapOf(
                        "id" to id,
                        "members" to members,
                        "messages" to listOf(message)
                    )
                    firestore.collection("ChatRooms").document(id).set(room).addOnSuccessListener {
                        Log.d("SIU","EXITO")
                    }
                    firestore.collection("Users").document(members[0]).update("chatRooms", FieldValue.arrayUnion(id))
                    firestore.collection("Users").document(members[1]).update("chatRooms", FieldValue.arrayUnion(id))
                } else {
                    var flag = false
                    it.result.documents.forEach{
                        val members = it["members"] as List<*>
                        if(members.contains(Utils.getUidLoggedIn())){
                            flag = true
                            firestore.collection("ChatRooms").document(it.id).update("messages", FieldValue.arrayUnion(message)).addOnCompleteListener {
                                Log.d("SIU","EXITO")
                            }
                        }
                    }
                    if(!flag){
                        val members = listOf(sId, Utils.getUidLoggedIn())
                        val id = UUID.randomUUID().toString()
                        val room = hashMapOf(
                            "id" to id,
                            "members" to members,
                            "messages" to listOf(message)
                        )
                        firestore.collection("ChatRooms").document(id).set(room).addOnSuccessListener {
                            Log.d("SIU","EXITO")
                        }
                        firestore.collection("Users").document(members[0]).update("chatRooms", FieldValue.arrayUnion(id))
                        firestore.collection("Users").document(members[1]).update("chatRooms", FieldValue.arrayUnion(id))
                    }
                }
            }
        }
    }
}