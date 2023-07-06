package com.example.funakhir.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.funakhir.databinding.ItemUserBinding
import com.example.funakhir.model.UsersItem

class UserAdapter(private val listUser: ArrayList<UsersItem>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<UsersItem>) {
        listUser.apply {
            clear()
            addAll(users)
        }
        notifyDataSetChanged()
    }

    fun setFavoriteList(users: List<UsersItem>) {
        listUser.apply {
            clear()
            addAll(users)
        }
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private var itemBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(user: UsersItem) {
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .circleCrop()
                .into(itemBinding.civAvatar)

            itemBinding.apply {
                tvUsername.text = user.username
            }

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listUser[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback{
        fun onItemClicked(data: UsersItem)
    }
}