package com.example.homework19.data.common

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class HandleResponse @Inject constructor() {
    suspend fun <T : Any> handleApiCall(apiCall: suspend () -> Response<T>): Flow<Resource<T>> =
        flow {
            val moshi: Moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val errorAdapter: JsonAdapter<String> = moshi.adapter(String::class.java)

            try {
                val response: Response<T> = apiCall()

                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    val serverErrorData = response.errorBody()?.string()
                    val errorResponse = errorAdapter.fromJson(serverErrorData ?: "")
                    val errorMessage = errorResponse ?: "Unknown error"

                    emit(Resource.Error("Code: ${response.code()}: $errorMessage"))
                }

            } catch (e: IOException) {
                emit(Resource.Error("Network error occurred: $e"))
            } catch (e: Exception) {
                emit(Resource.Error("An unexpected error occurred: $e"))
            }
        }
}



