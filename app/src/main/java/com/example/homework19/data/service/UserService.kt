package com.example.homework19.data.service

import com.example.homework19.data.model.UserInfoResponseDto
import com.example.homework19.data.model.UserListResponseDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @POST("7ec14eae-06bf-4f6d-86d2-ac1b9df5fe3d")
    suspend fun getUsers(): Response<List<UserListResponseDto>>

    @GET("users/{id}")
    suspend fun getUserInfo(@Path("id") id: Int): Response<UserInfoResponseDto>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}
