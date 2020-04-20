package com.mi.mvi.data.datasource.remote

import com.mi.mvi.remote.entity.UserResponse

interface AuthRemoteDataSource {

    suspend fun login(
        email: String,
        password: String
    ): UserResponse

    suspend fun register(
        email: String,
        username: String,
        password: String,
        password2: String
    ): UserResponse
}
