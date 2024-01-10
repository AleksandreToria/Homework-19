package com.example.homework19.data.mapper

import com.example.homework19.data.model.UserListResponseDto
import com.example.homework19.domain.model.GetUser

fun UserListResponseDto.toDomain(): GetUser {
    return GetUser(id, email, firstName, lastName, avatar, isSelected = false)
}