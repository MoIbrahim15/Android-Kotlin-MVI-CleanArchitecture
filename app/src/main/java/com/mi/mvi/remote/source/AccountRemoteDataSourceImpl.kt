package com.mi.mvi.remote.source

import com.mi.mvi.cache.entity.UserEntity
import com.mi.mvi.data.datasource.remote.AccountRemoteDataSource
import com.mi.mvi.remote.entity.BaseResponse
import com.mi.mvi.remote.service.AccountAPIService

class AccountRemoteDataSourceImpl(
    private val accountAPIService: AccountAPIService
) : AccountRemoteDataSource {

    override suspend fun getAccountProperties(authorization: String): UserEntity {
        return accountAPIService.getAccountProperties(authorization)
    }

    override suspend fun updateAccountProperties(
        authorization: String,
        email: String,
        username: String
    ): BaseResponse {
        return accountAPIService.updateAccountProperties(authorization, email, username)
    }

    override suspend fun changePassword(
        authorization: String,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): BaseResponse {
        return accountAPIService.changePassword(
            authorization,
            currentPassword,
            newPassword,
            confirmNewPassword
        )
    }
}
