package com.example.healthcare.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.ChattingActivity
import com.example.healthcare.Models.User
import com.example.healthcare.R

class UserAdapter(val context:Context, val userlist:ArrayList<User>):RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view:View=LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        var currentUser = userlist[position]

        holder.textName.text=currentUser.name
        holder.email.text=currentUser.email

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChattingActivity::class.java)

            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)
        }
    }

    class UserViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.txt_name)
        val email = itemView.findViewById<TextView>(R.id.email)
    }
}