package com.example.homework19.di

import com.example.homework19.data.service.UserService
import com.example.homework19.domain.repository.UserRepository
import com.example.homework19.domain.use_case.GetUserInfoUseCase
import com.example.homework19.domain.use_case.GetUsersUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModel {
    private const val BASE_URL_USER_LIST = "https://run.mocky.io/v3/"
    private const val BASE_URL_USER = "https://reqres.in/api/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    @Named("userList")
    fun provideRetrofitUserList(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_USER_LIST)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    @Named("user")
    fun provideRetrofitUserInfo(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_USER)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    @Named("provideUserListService")
    fun provideUserListService(@Named("userList") retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Named("provideUserInfoService")
    @Singleton
    @Provides
    fun provideUserInfoService(@Named("user") retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Named("provideDeleteUser")
    @Singleton
    @Provides
    fun provideDeleteUserService(@Named("user") retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideGetUsersUseCase(userRepository: UserRepository): GetUsersUseCase {
        return GetUsersUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(userRepository: UserRepository): GetUserInfoUseCase {
        return GetUserInfoUseCase(userRepository)
    }
}

