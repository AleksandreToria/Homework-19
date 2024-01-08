package com.example.homework19.presentation.model

import com.example.homework19.domain.model.UserList

data class SelectableUser(
    val user: UserList,
    var isSelected: Boolean = false
)
