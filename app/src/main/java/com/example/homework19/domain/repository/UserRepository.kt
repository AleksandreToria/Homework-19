package com.example.homework19.domain.repository

import com.example.homework19.data.common.Resource
import com.example.homework19.domain.model.GetUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): Flow<Resource<List<GetUser>>>
    suspend fun getUserInfo(id: Int): Flow<Resource<GetUser>>
    suspend fun deleteUser(id: Int): Flow<Resource<Unit>>
}