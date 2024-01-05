package com.example.homework19.data.repository

import com.example.homework19.data.common.HandleResponse
import com.example.homework19.data.common.Resource
import com.example.homework19.data.common.mapToDomain
import com.example.homework19.data.mapper.toDomain
import com.example.homework19.data.model.UserInfoResponseDto
import com.example.homework19.data.service.UserInfoService
import com.example.homework19.data.service.UserListService
import com.example.homework19.domain.user_list.UserList
import com.example.homework19.domain.user_list.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userListService: UserListService,
    private val handleResponse: HandleResponse,
    private val userInfoService: UserInfoService
) : UserRepository {
    override suspend fun getUsers(): Flow<Resource<List<UserList>>> {
        return handleResponse.handleApiCall {
            userListService.getUsers()
        }.mapToDomain { userListEntities ->
            userListEntities?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun getUserInfo(id: Int): Flow<Resource<UserList>> {
        return flow {
            try {
                val response: Response<UserInfoResponseDto> = userInfoService.getUserInfo(id)

                if (response.isSuccessful) {
                    val userInfoResponseDto = response.body()
                    val user = userInfoResponseDto?.data?.toDomain()
                        ?: throw NoSuchElementException("User not found")
                    emit(Resource.Success(user))
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody ?: "Unknown error"
                    emit(Resource.Error("Code: ${response.code()}: $errorMessage"))
                }

            } catch (e: IOException) {
                emit(Resource.Error("Network error occurred: $e"))
            } catch (e: Exception) {
                emit(Resource.Error("An unexpected error occurred: $e"))
            }
        }
    }
}