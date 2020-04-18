package com.mi.mvi.domain.repository

import com.mi.mvi.cache.entity.UserEntity
import com.mi.mvi.cache.entity.AuthTokenEntity
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.flow.Flow

interface AccountRepository : BaseRepository {

    fun getAccountProperties(authTokenEntity: AuthTokenEntity)
            : Flow<DataState<AccountViewState>>

    fun updateAccountProperties(
        authTokenEntity: AuthTokenEntity,
        userEntity: UserEntity
    ): Flow<DataState<AccountViewState>>

    fun changePassword(
        authTokenEntity: AuthTokenEntity,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<DataState<AccountViewState>>
}