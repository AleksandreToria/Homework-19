package com.example.homework19.domain.user_info

import com.example.homework19.data.common.Resource
import com.example.homework19.domain.user_list.UserList
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    suspend fun getUserInfo(id: Int): Flow<Resource<UserList>>
}
