package com.example.homework19.data.repository

import com.example.homework19.data.common.HandleResponse
import com.example.homework19.data.common.Resource
import com.example.homework19.data.common.infoMapToDomain
import com.example.homework19.data.common.mapToDomain
import com.example.homework19.data.mapper.toDomain
import com.example.homework19.data.service.UserService
import com.example.homework19.domain.model.UserList
import com.example.homework19.domain.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    @Named("provideUserListService") private val userService: UserService,
    @Named("provideUserInfoService") private val userInfoService: UserService,
    private val handleResponse: HandleResponse,
) : UserRepository {
    override suspend fun getUsers(): Flow<Resource<List<UserList>>> {
        return handleResponse.handleApiCall {
            userService.getUsers()
        }.mapToDomain { userListEntities ->
            userListEntities?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun getUserInfo(id: Int): Flow<Resource<UserList>> {
        return handleResponse.handleApiCall {
            userInfoService.getUserInfo(id)
        }.infoMapToDomain { userInfoResponseDto ->
            userInfoResponseDto?.data?.toDomain() ?: throw NoSuchElementException("User not found")
        }
    }
}