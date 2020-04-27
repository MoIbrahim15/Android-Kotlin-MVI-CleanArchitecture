package com.mi.mvi.data.datasource.remote

import com.mi.mvi.data.entity.BaseEntity
import com.mi.mvi.data.entity.UserEntity

interface AccountRemoteDataSource {

    suspend fun getAccountProperties(
        authorization: String
    ): UserEntity

    suspend fun updateAccountProperties(
        authorization: String,
        email: String?,
        username: String?
    ): BaseEntity

    suspend fun changePassword(
        authorization: String,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): BaseEntity
}
