package com.example.homework19.di

import com.example.homework19.data.common.HandleResponse
import com.example.homework19.data.repository.UserRepositoryImpl
import com.example.homework19.data.service.UserInfoService
import com.example.homework19.data.service.UserListService
import com.example.homework19.domain.user_list.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideUserListRepository(
        userListService: UserListService,
        userInfoService: UserInfoService,
        handleResponse: HandleResponse
    ): UserRepository {
        return UserRepositoryImpl(userListService, handleResponse, userInfoService)
    }

//    @Singleton
//    @Provides
//    fun provideUserInfoRepository(
//        userInfoService: UserInfoService,
//        handleResponse: HandleResponse
//    ): UserInfoRepository {
//        return UserInfoRepositoryImpl(userInfoService, handleResponse)
//    }
}