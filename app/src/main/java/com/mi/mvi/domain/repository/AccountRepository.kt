package com.mi.mvi.domain.repository

import com.mi.mvi.datasource.model.AccountProperties
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.flow.Flow

interface AccountRepository : BaseRepository {

    fun getAccountProperties(authToken: AuthToken)
            : Flow<DataState<AccountViewState>>

    fun updateAccountProperties(
        authToken: AuthToken,
        accountProperties: AccountProperties
    ): Flow<DataState<AccountViewState>>

    fun changePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<DataState<AccountViewState>>
}