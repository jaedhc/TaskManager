package com.example.taskmanager.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskmanager.R
import com.example.taskmanager.activities.Utils
import com.example.taskmanager.activities.data.model.MessageModel

class MessageAdapter : RecyclerView.Adapter<MessageHolder>(){
    private var listOfMessages = listOf<MessageModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        var view:View
        when(viewType){
            0 -> view = LayoutInflater.from(parent.context).inflate(R.layout.message_item0, parent, false)
            1 -> view = LayoutInflater.from(parent.context).inflate(R.layout.message_item1, parent, false)
            else -> view = LayoutInflater.from(parent.context).inflate(R.layout.message_item0, parent, false)
        }

        return MessageHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        var lay = 0
        if (listOfMessages[position].author == Utils.getUidLoggedIn()){
           lay = 1
        }
        return lay
    }

    override fun getItemCount(): Int = listOfMessages.size

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {

        holder.content.text = listOfMessages[position].content
    }

    fun setMessageList(list: List<MessageModel>){
        this.listOfMessages = list
        notifyDataSetChanged()
    }
}

class MessageHolder(itemView: View): ViewHolder(itemView) {
    val content:TextView = itemView.findViewById(R.id.txt_content)
}