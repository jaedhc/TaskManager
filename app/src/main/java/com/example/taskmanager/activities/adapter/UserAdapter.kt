package com.example.taskmanager.activities.adapter

import android.annotation.SuppressLint
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
import com.example.taskmanager.activities.data.model.UsersModel

class UserAdapter : RecyclerView.Adapter<UserHolder>() {

    private var listOfUsers = listOf<UsersModel>()
    private var listener: OnUserClickListener? = null
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfUsers.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val users = listOfUsers[position]

        val name = users.userName
        val img = users.imageUrl
        val status = users.status

        context = holder.itemView.context



        if (status.equals("Online")){
            holder.status.text = context.resources.getString(R.string.status_offline)
        } else{
            holder.status.text = context.resources.getString(R.string.status_offline)
        }



        if(img!!.isNotEmpty()){
            Glide.with(holder.itemView.context)
                .load(img)
                .circleCrop()
                .into(holder.image)
        }
        holder.profName.text = name

        holder.itemView.setOnClickListener {
            listener?.onUserSelected(position, users)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUserList(list: List<UsersModel>){
        this.listOfUsers = list
        notifyDataSetChanged()
    }

    fun setOnUserClickListener(listener: OnUserClickListener){
        this.listener = listener
    }

    interface OnUserClickListener{
        fun onUserSelected(position: Int, users: UsersModel)

    }

}

class UserHolder(itemView: View) : ViewHolder(itemView){
    val profName = itemView.findViewById<TextView>(R.id.chat_item_name)
    val status = itemView.findViewById<TextView>(R.id.chat_item_last_message)
    val image = itemView.findViewById<ImageView>(R.id.chat_item_img)
}
