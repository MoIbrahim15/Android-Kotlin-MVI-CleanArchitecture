package com.mi.mvi.data.datasource.remote

import com.mi.mvi.datasource.model.LoginResponse
import com.mi.mvi.datasource.model.RegisterResponse

interface AuthRemoteDataSource {

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse

    suspend fun register(
        email: String,
        username: String,
        password: String,
        password2: String
    ): RegisterResponse

}