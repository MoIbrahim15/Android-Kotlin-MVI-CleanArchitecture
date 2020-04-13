package com.mi.mvi.data.datasource.remote

import com.mi.mvi.datasource.model.AccountProperties
import com.mi.mvi.datasource.model.BaseResponse

interface AccountRemoteDataSource {

    suspend fun getAccountProperties(
        authorization: String
    ): AccountProperties

    suspend fun updateAccountProperties(
        authorization: String,
        email: String,
        username: String
    ): BaseResponse


    suspend fun changePassword(
        authorization: String,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): BaseResponse

}