package com.mi.mvi.data.repository.auth

import androidx.lifecycle.liveData
import com.mi.mvi.data.database.AccountDao
import com.mi.mvi.data.database.AuthTokenDao
import com.mi.mvi.data.network.ResponseHandler
import com.mi.mvi.data.network.auth.AuthApiService
import com.mi.mvi.data.network.responses.RegistrationResponse
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.utils.Resource
import kotlinx.coroutines.Dispatchers

class AuthRepository(
    val authTokenDao: AuthTokenDao,
    val accountDao: AccountDao,
    val apiService: AuthApiService,
    val sessionManager: SessionManager,
    val responseHandler: ResponseHandler
) {

    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.LOADING(true))
        try {
            val response = apiService.login(email, password)
            emit(responseHandler.handleSuccess(response))
        } catch (e: Exception) {
            emit(responseHandler.handleException(e))
        }
    }

    suspend fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): RegistrationResponse {
        return apiService.register(email, username, password, confirmPassword)
    }
}
