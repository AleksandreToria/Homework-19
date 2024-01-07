package com.example.homework19.domain.use_case

import com.example.homework19.data.common.Resource
import com.example.homework19.domain.model.UserList
import com.example.homework19.domain.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun execute(): Flow<Resource<List<UserList>>> {
        return userRepository.getUsers()
    }
}
