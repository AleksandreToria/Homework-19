package com.example.homework19.data.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, DomainType> Flow<Resource<List<T>>>.mapToDomain(mapper: (List<T>?) -> List<DomainType>): Flow<Resource<List<DomainType>>> {
    return this.map { resource ->
        when (resource) {
            is Resource.Success -> Resource.Success(mapper(resource.data))
            is Resource.Error -> Resource.Error(resource.errorMessage)
            is Resource.Loading -> Resource.Loading
        }
    }
}

