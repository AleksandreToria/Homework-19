package com.example.homework19.data.service

import com.example.homework19.data.common.Constants
import com.example.homework19.data.model.UserInfoResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserInfoService {
    @GET(Constants.USER_INFO)
    suspend fun getUserInfo(@Path("id") id: Int): Response<UserInfoResponseDto>
}