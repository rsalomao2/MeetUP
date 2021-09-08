package com.example.meetup.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meetup.R
import com.example.meetup.model.UserListItem
import kotlinx.android.synthetic.main.user_row.view.*

class UsersAdapters(
    private val userListItem: List<UserListItem>,
    private val onClickItem: (UserListItem) -> Unit
) :
    RecyclerView.Adapter<UsersAdapters.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.firstName
        val id: TextView = itemView.idTextView
        val username: TextView = itemView.userName
        val email: TextView = itemView.email
        val image: ImageView = itemView.listRowImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userListItem = userListItem[position]

        val nameText = userListItem.name
        val userNameText = userListItem.userNameText
        val emailText = userListItem.emailText
        val idText = userListItem.idText

        holder.name.text = nameText
        holder.username.text = userNameText
        holder.id.text = idText
        holder.email.text = emailText
        Glide.with(holder.itemView.context)
            .load(userListItem.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.image)
        holder.itemView.setOnClickListener {
            onClickItem(userListItem)
        }
    }

    override fun getItemCount() = userListItem.size
}
