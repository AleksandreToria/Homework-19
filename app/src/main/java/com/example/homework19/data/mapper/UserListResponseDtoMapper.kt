package com.example.homework19.data.mapper

import com.example.homework19.data.model.UserListResponseDto
import com.example.homework19.domain.user_list.UserList

fun UserListResponseDto.toDomain(): UserList {
    return UserList(id, email, firstName, lastName, avatar)
}