package com.mi.mvi.remote.source

import com.mi.mvi.data.datasource.remote.AuthRemoteDataSource
import com.mi.mvi.data.entity.UserEntity
import com.mi.mvi.remote.mapper.UserEntityMapper
import com.mi.mvi.remote.service.AuthAPIService

class AuthRemoteDataSourceImpl(
    private val authAPIService: AuthAPIService,
    private val userEntityMapper: UserEntityMapper
) : AuthRemoteDataSource {

    override suspend fun login(email: String, password: String): UserEntity {
        return userEntityMapper.mapFromRemote(
            authAPIService.login(
                email,
                password
            )
        )
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String,
        password2: String
    ): UserEntity {
        return userEntityMapper.mapFromRemote(
            authAPIService.register(
                email,
                username,
                password,
                password2
            )
        )
    }
}
