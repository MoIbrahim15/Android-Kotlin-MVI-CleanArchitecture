package com.mi.mvi.data.repository.auth

import androidx.lifecycle.liveData
import com.mi.mvi.data.database.AccountDao
import com.mi.mvi.data.database.AuthTokenDao
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.network.auth.AuthApiService
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.response_handler.ResponseHandler
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.ui.auth.state.AuthViewState
import kotlinx.coroutines.Dispatchers

class AuthRepository(
    val authTokenDao: AuthTokenDao,
    val accountDao: AccountDao,
    val apiService: AuthApiService,
    val sessionManager: SessionManager,
    val responseHandler: ResponseHandler
) {

    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(DataState.LOADING(true))
        try {
            val response = apiService.login(email, password)
            if (response.response != "Error") {
                emit(
                    responseHandler.handleSuccess(
                        AuthViewState(
                            authToken = AuthToken(
                                response.pk,
                                response.token
                            )
                        )
                    )
                )
            } else {
                emit(responseHandler.handleError(Exception(response.errorMessage)))
            }
        } catch (e: Exception) {
            emit(responseHandler.handleError(e))
        }
    }

    fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ) = liveData(Dispatchers.IO) {
        emit(DataState.LOADING(true))
        try {
            val response = apiService.register(email, username, password, confirmPassword)
            if (response.response != "Error") {
                emit(
                    responseHandler.handleSuccess(
                        AuthViewState(
                            authToken = AuthToken(
                                response.pk,
                                response.token
                            )
                        )
                    )
                )
            } else {
                emit(responseHandler.handleError(Exception(response.errorMessage)))
            }
        } catch (e: Exception) {
            emit(responseHandler.handleError(e))
        }
    }
}
