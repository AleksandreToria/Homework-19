package com.example.homework19.domain.model

data class GetUser(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatar: String,
    val isSelected: Boolean
)