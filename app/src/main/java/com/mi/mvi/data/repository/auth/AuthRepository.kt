package com.mi.mvi.data.repository.auth

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.mi.mvi.data.database.AccountDao
import com.mi.mvi.data.database.AuthTokenDao
import com.mi.mvi.data.models.Account
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.network.auth.AuthApiService
import com.mi.mvi.data.network.responses.BaseResponse
import com.mi.mvi.data.network.responses.LoginResponse
import com.mi.mvi.data.network.responses.RegisterResponse
import com.mi.mvi.data.preference.SharedPreferenceKeys.Companion.PREVIOUS_AUTH_USER
import com.mi.mvi.data.repository.BaseRepository
import com.mi.mvi.data.repository.NetworkBoundResource
import com.mi.mvi.data.response_handler.*
import com.mi.mvi.data.response_handler.ResponseEntity.NONE
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.ui.auth.state.AuthViewState
import com.mi.mvi.ui.auth.state.LoginFields
import com.mi.mvi.ui.auth.state.RegistrationFields

class AuthRepository(
    private val authTokenDao: AuthTokenDao,
    private val accountDao: AccountDao,
    private val apiService: AuthApiService,
    private val sessionManager: SessionManager,
    private val responseHandler: ResponseHandler,
    private val sharedPreferences: SharedPreferences,
    private val sharedPrefsEditor: SharedPreferences.Editor
) : BaseRepository() {

    fun login(email: String, password: String): LiveData<DataState<AuthViewState>> {
        val loginError = LoginFields(email, password).loginError()
        if (loginError !is NONE) {
            return returnErrorResponse(loginError, ResponseView.DIALOG())
        }
        return object : NetworkBoundResource<LoginResponse, AuthViewState>(
            responseHandler,
            sessionManager.isConnectedToInternet(),
            true
        ) {
            override suspend fun createNetworkRequest(): LoginResponse {
                return apiService.login(email, password)
            }

            override fun handleSuccess(response: LoginResponse): DataState<AuthViewState> {
                accountDao.insertOrIgnore(Account(response.pk, response.email, ""))
                val result = authTokenDao.insert(AuthToken(response.pk, response.token))
                if (result < 0) {
                    return DataState.ERROR(
                        Response(
                            ResponseEntity.CAN_NOT_SAVE(),
                            ResponseView.DIALOG()
                        )
                    )
                }
                saveAuthUserToPrefs(response.email)
                return DataState.SUCCESS(
                    AuthViewState(
                        authToken = AuthToken(
                            account_pk = response.pk,
                            token = response.token
                        )
                    )
                )
            }

            override suspend fun createCacheRequest(): DataState<AuthViewState> {
                return DataState.SUCCESS(response = Response(NONE(), ResponseView.NONE()))
            }

        }.call()
    }

    fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {
        val registerError =
            RegistrationFields(email, username, password, confirmPassword).registerError()
        if (registerError !is NONE) {
            return returnErrorResponse(registerError, ResponseView.DIALOG())
        }
        return object : NetworkBoundResource<RegisterResponse, AuthViewState>(
            responseHandler,
            sessionManager.isConnectedToInternet(),
            true
        ) {
            override suspend fun createNetworkRequest(): RegisterResponse {
                return apiService.register(email, username, password, confirmPassword)
            }

            override fun handleSuccess(response: RegisterResponse): DataState<AuthViewState> {
                accountDao.insertOrIgnore(Account(response.pk, response.email, ""))
                val result = authTokenDao.insert(AuthToken(response.pk, response.token))
                if (result < 0) {
                    return DataState.ERROR(
                        Response(
                            ResponseEntity.CAN_NOT_SAVE(),
                            ResponseView.DIALOG()
                        )
                    )
                }
                saveAuthUserToPrefs(response.email)
                return DataState.SUCCESS(
                    AuthViewState(
                        authToken = AuthToken(
                            account_pk = response.pk,
                            token = response.token
                        )
                    )
                )
            }

            override suspend fun createCacheRequest(): DataState<AuthViewState> {
                return DataState.SUCCESS(response = Response(NONE(), ResponseView.NONE()))
            }

        }.call()
    }

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>> {
        val previousAuthUserEmail = sharedPreferences.getString(PREVIOUS_AUTH_USER, null)
        if (previousAuthUserEmail.isNullOrBlank())
            return returnErrorResponse(NONE(), ResponseView.NONE())
        return object : NetworkBoundResource<BaseResponse, AuthViewState>(
            responseHandler,
            sessionManager.isConnectedToInternet(),
            false
        ) {
            override suspend fun createNetworkRequest(): BaseResponse? {
                return null
            }

            override fun handleSuccess(response: BaseResponse): DataState<AuthViewState> {
                return DataState.SUCCESS()
            }

            override suspend fun createCacheRequest(): DataState<AuthViewState> {
                accountDao.searchByEmail(previousAuthUserEmail)?.let { account ->
                    if (account.pk > -1) {
                        authTokenDao.searchByPk(account.pk)?.let { authToken ->
                            return DataState.SUCCESS(AuthViewState(authToken = authToken))
                        }
                    }
                }
                return DataState.SUCCESS(response = Response(NONE(), ResponseView.NONE()))
            }

        }.call()
    }

    private fun saveAuthUserToPrefs(email: String) {
        sharedPrefsEditor.putString(PREVIOUS_AUTH_USER, email)
        sharedPrefsEditor.apply()
    }
}
