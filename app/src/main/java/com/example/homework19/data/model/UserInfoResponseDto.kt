package com.example.homework19.data.model

import com.squareup.moshi.Json

data class UserInfoResponseDto(
    @Json(name = "data")
    val data: UserListResponseDto
)