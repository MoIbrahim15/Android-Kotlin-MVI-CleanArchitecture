package com.mi.mvi.data.repository.auth

import com.mi.mvi.data.database.AccountDao
import com.mi.mvi.data.database.AuthTokenDao
import com.mi.mvi.data.network.auth.AuthApiService
import com.mi.mvi.data.session.SessionManager

class AuthRepository(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountDao,
    val apiService: AuthApiService,
    val sessionManager: SessionManager
) {


}
