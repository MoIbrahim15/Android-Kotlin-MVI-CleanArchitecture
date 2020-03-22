package com.mi.mvi.data.repository.main

import com.mi.mvi.data.database.AccountDao
import com.mi.mvi.data.network.main.MainApiService
import com.mi.mvi.data.repository.BaseRepository
import com.mi.mvi.data.response_handler.ErrorHandler
import com.mi.mvi.data.session.SessionManager

class AccountRepository(
    val apiService: MainApiService,
    val accountDao: AccountDao,
    val sessionManager: SessionManager,
    val errorHandler: ErrorHandler
) : BaseRepository() {
}