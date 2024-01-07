package com.example.homework19.domain.user

import com.example.homework19.data.common.Resource
import com.example.homework19.domain.model.UserList
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): Flow<Resource<List<UserList>>>
    suspend fun getUserInfo(id: Int): Flow<Resource<UserList>>
}