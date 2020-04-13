package com.mi.mvi.datasource.remote.account

import com.mi.mvi.data.datasource.remote.AccountRemoteDataSource
import com.mi.mvi.datasource.model.AccountProperties
import com.mi.mvi.datasource.model.BaseResponse

class AccountRemoteDataSourceImpl(
    private val accountAPIService: AccountAPIService
) : AccountRemoteDataSource {

    override suspend fun getAccountProperties(authorization: String): AccountProperties {
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