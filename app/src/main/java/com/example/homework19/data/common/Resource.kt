package com.example.homework19.data.common

sealed class Resource<out D> {
    data class Success<out D>(val data: D) : Resource<D>()
    data class Loading(val isLoading: Boolean) : Resource<Nothing>()
    data class Error<out D>(val errorMessage: String) : Resource<D>()
}
