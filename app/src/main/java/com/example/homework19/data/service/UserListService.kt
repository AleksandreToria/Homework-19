package com.example.homework19.data.service

import com.example.homework19.data.model.UserListResponseDto
import retrofit2.Response
import retrofit2.http.POST

interface UserListService {
    @POST("7ec14eae-06bf-4f6d-86d2-ac1b9df5fe3d")
    suspend fun getUsers(): Response<List<UserListResponseDto>>
}