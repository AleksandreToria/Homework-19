package com.example.homework19.presentation.state

import com.example.homework19.domain.model.GetUser

data class UserListState(
    val users: List<GetUser> = emptyList(),
    val selectedUsers: Set<Int> = emptySet(),
)