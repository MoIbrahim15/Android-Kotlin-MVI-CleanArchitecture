package com.mi.mvi.data.repository.main

import com.mi.mvi.data.database.AccountDao
import com.mi.mvi.data.models.AccountProperties
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.network.main.MainApiService
import com.mi.mvi.data.repository.BaseRepository
import com.mi.mvi.data.repository.NetworkBoundResource
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.response_handler.ErrorHandler
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.ui.main.account.state.AccountViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountRepository(
    val apiService: MainApiService,
    val accountDao: AccountDao,
    val sessionManager: SessionManager,
    val errorHandler: ErrorHandler
) : BaseRepository() {

    fun getAccountProperties(authToken: AuthToken): Flow<DataState<AccountViewState>> = flow {

    }
}