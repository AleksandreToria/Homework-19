package com.example.homework19.di

import com.example.homework19.data.common.HandleResponse
import com.example.homework19.data.repository.UserRepositoryImpl
import com.example.homework19.data.service.UserService
import com.example.homework19.domain.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideUserListRepository(
        @Named("provideUserListService") userService: UserService,
        @Named("provideUserInfoService") userInfoService: UserService,
        handleResponse: HandleResponse
    ): UserRepository {
        return UserRepositoryImpl(userService, userInfoService, handleResponse)
    }
}

