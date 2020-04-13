package com.mi.mvi.datasource.remote.auth

import com.mi.mvi.data.datasource.remote.AuthRemoteDataSource
import com.mi.mvi.datasource.model.LoginResponse
import com.mi.mvi.datasource.model.RegisterResponse

class AuthRemoteDataSourceImpl(
    private val authAPIService: AuthAPIService
) : AuthRemoteDataSource {
    override suspend fun login(email: String, password: String): LoginResponse {
        return authAPIService.login(email, password)
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String,
        password2: String
    ): RegisterResponse {
        return authAPIService.register(email, username, password, password2)
    }
}