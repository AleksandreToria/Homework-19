package com.example.homework19.data.common.map_to_domain

import com.example.homework19.data.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <any, DomainType> Flow<Resource<any>>.mapInfoToDomain(mapper: (any) -> DomainType): Flow<Resource<DomainType>> {
    return this.map { resource ->
        when (resource) {
            is Resource.Success -> Resource.Success(mapper(resource.data))
            is Resource.Error -> Resource.Error(resource.errorMessage)
            is Resource.Loading -> Resource.Loading(resource.isLoading)
        }
    }
}