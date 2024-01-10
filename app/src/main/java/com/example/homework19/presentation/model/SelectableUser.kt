package com.example.homework19.presentation.model

import com.example.homework19.domain.model.GetUser

data class SelectableUser(
    val user: GetUser,
    var isSelected: Boolean = false
)
