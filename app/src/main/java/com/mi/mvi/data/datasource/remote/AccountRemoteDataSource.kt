package com.mi.mvi.data.datasource.remote

import com.mi.mvi.cache.entity.UserEntity
import com.mi.mvi.remote.entity.BaseResponse

interface AccountRemoteDataSource {

    suspend fun getAccountProperties(
        authorization: String
    ): UserEntity

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
