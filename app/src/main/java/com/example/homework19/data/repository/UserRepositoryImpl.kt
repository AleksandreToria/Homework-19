package com.example.homework19.data.repository

import com.example.homework19.data.common.HandleResponse
import com.example.homework19.data.common.Resource
import com.example.homework19.data.common.map_to_domain.mapInfoToDomain
import com.example.homework19.data.common.map_to_domain.mapListToDomain
import com.example.homework19.data.mapper.toDomain
import com.example.homework19.data.service.UserService
import com.example.homework19.domain.model.GetUser
import com.example.homework19.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    @Named("provideUserListService") private val userListService: UserService,
    @Named("provideUserInfoService") private val userInfoService: UserService,
    @Named("provideDeleteUser") private val userDeleteService: UserService,
    private val handleResponse: HandleResponse,
) : UserRepository {
    override suspend fun getUsers(): Flow<Resource<List<GetUser>>> {
        return handleResponse.handleApiCall {
            userListService.getUsers()
        }.mapListToDomain { userListEntities ->
            userListEntities.map { it.toDomain() }
        }
    }

    override suspend fun getUserInfo(id: Int): Flow<Resource<GetUser>> {
        return handleResponse.handleApiCall {
            userInfoService.getUserInfo(id)
        }.mapInfoToDomain { userInfoResponseDto ->
            userInfoResponseDto.data.toDomain()
        }
    }

    override suspend fun deleteUser(id: Int): Flow<Resource<Unit>> {
        return handleResponse.handleApiCall {
            userDeleteService.deleteUser(id)
        }
    }
}