package com.example.homework19.domain.use_case

import com.example.homework19.data.common.Resource
import com.example.homework19.domain.model.GetUser
import com.example.homework19.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: Int): Flow<Resource<GetUser>> {
        return userRepository.getUserInfo(id)
    }
}