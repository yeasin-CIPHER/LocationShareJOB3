package com.example.locationsharejob3f.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.locationsharejob3f.R

class UserAdapter(private var userList: List<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    fun updateData(newList: List<User>){
        userList = newList
        notifyDataSetChanged()
    }
    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
       private val tvName = itemView.findViewById<TextView>(R.id.nameEt)
       private val tvEmail = itemView.findViewById<TextView>(R.id.etEmail)
       private val tvLocation = itemView.findViewById<TextView>(R.id.etLocation)

        fun bind(user: User){
            tvName.text = user.name
            tvEmail.text = user.email
            tvLocation.text = user.location
        }
    }

}


