package com.example.homework19.domain.user_list

import com.example.homework19.data.common.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): Flow<Resource<List<UserList>>>
    suspend fun getUserInfo(id: Int): Flow<Resource<UserList>>
}