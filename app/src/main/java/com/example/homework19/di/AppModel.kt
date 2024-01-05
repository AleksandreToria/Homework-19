package com.example.homework19.di

import com.example.homework19.data.service.UserInfoService
import com.example.homework19.data.service.UserListService
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
    private const val BASE_URL_USER_INFO = "https://reqres.in/api/"

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
    @Named("userInfo")
    fun provideRetrofitUserInfo(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_USER_INFO)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideUserListService(@Named("userList") retrofit: Retrofit): UserListService {
        return retrofit.create(UserListService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserInfoService(@Named("userInfo") retrofit: Retrofit): UserInfoService {
        return retrofit.create(UserInfoService::class.java)
    }
}

