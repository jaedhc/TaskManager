package com.example.taskmanager.activities.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.taskmanager.R
import com.example.taskmanager.activities.data.model.ChatModel
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter : RecyclerView.Adapter<ChatHolder>() {
    private var listOfChats = listOf<ChatModel>()
    private var listener: OnChatClickListener? = null
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatHolder(view)
    }

    override fun getItemCount(): Int = listOfChats.size

    fun setChatList(list: List<ChatModel>){
        this.listOfChats = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        val chat = listOfChats[position]
        holder.name.text = chat.name
        holder.msg.text = chat.lastMsg

        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = format.format(chat.time.toDate())

        holder.time.text = formattedTime.toString()

        if(chat.img.isNotEmpty()){
            Glide.with(holder.itemView.context)
                .load(chat.img)
                .circleCrop()
                .into(holder.img)
        }

        holder.itemView.setOnClickListener {
            listener?.onChatSelected(position, chat)
        }
    }

    fun setOnChatClickListener(listener: OnChatClickListener){
        this.listener = listener
    }

    interface OnChatClickListener{
        fun onChatSelected(position: Int, chat: ChatModel)

    }

}

class ChatHolder(itemView: View): ViewHolder(itemView){
    val name = itemView.findViewById<TextView>(R.id.chat_item_name)
    val img = itemView.findViewById<ImageView>(R.id.chat_item_img)
    val msg = itemView.findViewById<TextView>(R.id.chat_item_last_message)
    val time = itemView.findViewById<TextView>(R.id.chat_item_time)
}