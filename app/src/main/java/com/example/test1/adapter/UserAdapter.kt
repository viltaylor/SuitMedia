package com.example.test1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test1.databinding.ItemUserBinding
import com.example.test1.model.User

class UserAdapter(private val onUserClick: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<User>()

    fun submitList(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    class UserViewHolder(
        private val binding: ItemUserBinding,
        private val onUserClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.nameTextView.text = "${user.firstName} ${user.lastName}"
            binding.emailTextView.text = user.email
            Glide.with(itemView.context)
                .load(user.avatar)
                .circleCrop()
                .into(binding.avatarImageView)
            itemView.setOnClickListener { onUserClick(user) }
        }
    }
}