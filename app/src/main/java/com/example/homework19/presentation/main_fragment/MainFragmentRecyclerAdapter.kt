package com.example.homework19.presentation.main_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homework19.databinding.ItemLayoutBinding
import com.example.homework19.domain.user_list.UserList

class MainFragmentRecyclerAdapter() :
    ListAdapter<UserList, MainFragmentRecyclerAdapter.UserListViewHolder>(DiffCallBack()) {

    private var onItemClick: ((UserList) -> Unit)? = null

    fun setOnItemClickListener(listener: (UserList) -> Unit) {
        onItemClick = listener
    }

    inner class UserListViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userList: UserList) {
            with(binding) {
                Glide.with(itemView)
                    .load(userList.avatar)
                    .into(image)

                email.text = userList.email
                firstName.text = userList.firstName
                lastName.text = userList.lastName

                root.setOnClickListener {
                    onItemClick?.invoke(userList)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    private class DiffCallBack : DiffUtil.ItemCallback<UserList>() {
        override fun areItemsTheSame(
            oldItem: UserList,
            newItem: UserList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserList,
            newItem: UserList
        ): Boolean {
            return oldItem == newItem
        }
    }
}
