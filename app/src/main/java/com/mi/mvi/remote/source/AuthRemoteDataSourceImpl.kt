package com.mi.mvi.remote.source

import com.mi.mvi.data.datasource.remote.AuthRemoteDataSource
import com.mi.mvi.remote.entity.UserResponse
import com.mi.mvi.remote.service.AuthAPIService

class AuthRemoteDataSourceImpl(
    private val authAPIService: AuthAPIService
) : AuthRemoteDataSource {
    override suspend fun login(email: String, password: String): UserResponse {
        return authAPIService.login(email, password)
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String,
        password2: String
    ): UserResponse {
        return authAPIService.register(email, username, password, password2)
    }
}
