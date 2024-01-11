package com.example.homework19.presentation.user_list_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homework19.databinding.ItemLayoutBinding
import com.example.homework19.domain.model.GetUser
import com.example.homework19.presentation.model.SelectableUser

class UserListRecyclerAdapter :
    ListAdapter<SelectableUser, UserListRecyclerAdapter.UserListViewHolder>(DiffCallBack()) {

    private var onItemClick: ((GetUser) -> Unit)? = null
    private var onCheckedChangeListener: ((Int, Boolean) -> Unit)? = null

    fun setOnCheckedChangeListener(listener: (position: Int, isChecked: Boolean) -> Unit) {
        onCheckedChangeListener = listener
    }

    fun setOnItemClickListener(listener: (GetUser) -> Unit) {
        this.onItemClick = listener
    }

    inner class UserListViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(selectableUser: SelectableUser) {
            with(binding) {
                Glide.with(itemView)
                    .load(selectableUser.user.avatar)
                    .into(image)

                email.text = selectableUser.user.email
                firstName.text = selectableUser.user.firstName
                lastName.text = selectableUser.user.lastName

                checkBox.isChecked = selectableUser.isSelected
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onCheckedChangeListener?.invoke(adapterPosition, isChecked)
                }

                root.setOnClickListener {
                    onItemClick?.invoke(selectableUser.user)
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

    private class DiffCallBack : DiffUtil.ItemCallback<SelectableUser>() {
        override fun areItemsTheSame(
            oldItem: SelectableUser,
            newItem: SelectableUser
        ): Boolean {
            return oldItem.user.id == newItem.user.id
        }

        override fun areContentsTheSame(
            oldItem: SelectableUser,
            newItem: SelectableUser
        ): Boolean {
            return oldItem == newItem
        }
    }
}
