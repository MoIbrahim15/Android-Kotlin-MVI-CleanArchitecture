package com.mi.mvi.remote.source

import com.mi.mvi.data.datasource.remote.AccountRemoteDataSource
import com.mi.mvi.data.entity.BaseEntity
import com.mi.mvi.data.entity.UserEntity
import com.mi.mvi.remote.mapper.BaseEntityMapper
import com.mi.mvi.remote.mapper.UserEntityMapper
import com.mi.mvi.remote.service.AccountAPIService

class AccountRemoteDataSourceImpl(
    private val accountAPIService: AccountAPIService,
    private val userEntityMapper: UserEntityMapper,
    private val baseEntityMapper: BaseEntityMapper
) : AccountRemoteDataSource {

    override suspend fun getAccountProperties(authorization: String): UserEntity {
        return userEntityMapper.mapFromRemote(accountAPIService.getAccountProperties(authorization))
    }

    override suspend fun updateAccountProperties(
        authorization: String,
        email: String?,
        username: String?
    ): BaseEntity {
        return baseEntityMapper.mapFromRemote(
            accountAPIService.updateAccountProperties(
                authorization,
                email,
                username
            )
        )
    }

    override suspend fun changePassword(
        authorization: String,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): BaseEntity {
        return baseEntityMapper.mapFromRemote(
            accountAPIService.changePassword(
                authorization,
                currentPassword,
                newPassword,
                confirmNewPassword
            )
        )
    }
}
