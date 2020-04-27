package com.mi.mvi.domain.repository

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.model.User
import com.mi.mvi.domain.viewstate.AccountViewState
import kotlinx.coroutines.flow.Flow

interface AccountRepository : BaseRepository {

    fun getAccountProperties(token: Token):
            Flow<DataState<AccountViewState>>

    fun updateAccountProperties(
        token: Token,
        user: User
    ): Flow<DataState<AccountViewState>>

    fun changePassword(
        token: Token,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<DataState<AccountViewState>>
}
