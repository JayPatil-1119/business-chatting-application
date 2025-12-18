package com.example.bchatbox

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser  = userList[position]
        holder.textName.text = currentUser .name

        // Show or hide the blue dot based on unread messages
        holder.itemView.findViewById<View>(R.id.blue_dot).visibility =
            if (currentUser .hasUnreadMessages) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            // When the user clicks, mark messages as read
            currentUser .hasUnreadMessages = false // Reset unread status
            notifyItemChanged(position) // Notify the adapter to refresh the item

            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser .name)
            intent.putExtra("uid", currentUser .uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.txt_name)
    }
}