package com.mi.mvi.data.datasource.remote

import com.mi.mvi.data.entity.UserEntity

interface AuthRemoteDataSource {

    suspend fun login(
        email: String,
        password: String
    ): UserEntity

    suspend fun register(
        email: String,
        username: String,
        password: String,
        password2: String
    ): UserEntity
}
