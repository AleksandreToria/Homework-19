package com.example.homework19.presentation.event

sealed class UserListFragmentEvents {
    data class SelectUserEvent(val id: Int, val isSelected: Boolean) : UserListFragmentEvents()
    data object DeleteUserEvent : UserListFragmentEvents()
}