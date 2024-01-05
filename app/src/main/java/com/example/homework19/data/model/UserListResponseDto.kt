package com.example.homework19.data.model

import com.squareup.moshi.Json

data class UserListResponseDto(
    val id: Int,
    val email: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    val avatar: String
)