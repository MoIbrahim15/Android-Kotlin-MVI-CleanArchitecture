package com.mi.mvi.data.repository.auth

import android.content.SharedPreferences
import com.mi.mvi.R
import com.mi.mvi.data.database.AccountDao
import com.mi.mvi.data.database.AuthTokenDao
import com.mi.mvi.data.models.AccountProperties
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.network.auth.AuthApiService
import com.mi.mvi.data.network.responses.BaseResponse
import com.mi.mvi.data.network.responses.LoginResponse
import com.mi.mvi.data.network.responses.RegisterResponse
import com.mi.mvi.data.preference.SharedPreferenceKeys.Companion.PREVIOUS_AUTH_USER
import com.mi.mvi.data.repository.BaseRepository
import com.mi.mvi.data.repository.NetworkBoundResource
import com.mi.mvi.data.response_handler.*
import com.mi.mvi.data.response_handler.ErrorConstants.Companion.SUCCESS_CODE
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.ui.auth.state.AuthViewState
import com.mi.mvi.ui.auth.state.LoginFields
import com.mi.mvi.ui.auth.state.RegistrationFields
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class AuthRepository(
    private val authTokenDao: AuthTokenDao,
    private val accountDao: AccountDao,
    private val apiService: AuthApiService,
    private val sessionManager: SessionManager,
    private val errorHandler: ErrorHandler,
    private val sharedPreferences: SharedPreferences,
    private val sharedPrefsEditor: SharedPreferences.Editor
) : BaseRepository() {

    fun login(email: String, password: String): Flow<DataState<AuthViewState>> = flow {
        val loginError = LoginFields(email, password).loginError()
        if (loginError != SUCCESS_CODE) {
            emit(DataState.ERROR(Response(loginError, ResponseView.DIALOG())))
        } else {
            val networkBoundResource = object : NetworkBoundResource<LoginResponse, AuthViewState>(
                errorHandler,
                sessionManager.isConnectedToInternet(),
                true
            ) {
                override suspend fun createNetworkRequest(): LoginResponse {
                    return apiService.login(email, password)
                }

                override suspend fun handleSuccess(response: LoginResponse) {
                    if (response.response != ErrorConstants.GENERIC_AUTH_ERROR) {
                        accountDao.insertOrIgnore(AccountProperties(response.pk, response.email, ""))
                        val result = authTokenDao.insert(AuthToken(response.pk, response.token))
                        if (result < 0) {
                            emit(
                                    DataState.ERROR(
                                            Response(
                                                    R.string.error_something_went_wrong,
                                                    ResponseView.DIALOG()
                                            )
                                    )
                            )
                        } else {
                            saveAuthUserToPrefs(response.email)
                            emit(
                                    DataState.SUCCESS(
                                            AuthViewState(
                                                    authToken = AuthToken(
                                                            account_pk = response.pk,
                                                            token = response.token
                                                    )
                                            )
                                    )
                            )

                        }
                    } else {
                        emit(errorHandler.invoke(message = response.errorMessage))
                    }
                }

                override suspend fun createCacheRequest() {

                }

            }
            emitAll(networkBoundResource.call())
        }
    }


    fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>> = flow {
        val registerError = RegistrationFields(email, username, password, confirmPassword).registerError()
        if (registerError != SUCCESS_CODE) {
            emit(DataState.ERROR(Response(registerError, ResponseView.DIALOG())))
        } else {
            val networkBoundResource =
                object : NetworkBoundResource<RegisterResponse, AuthViewState>(
                    errorHandler,
                    sessionManager.isConnectedToInternet(),
                    true
                ) {
                    override suspend fun createNetworkRequest(): RegisterResponse {
                        return apiService.register(email, username, password, confirmPassword)
                    }

                    override suspend fun handleSuccess(response: RegisterResponse) {
                        accountDao.insertOrIgnore(AccountProperties(response.pk, response.email, ""))
                        val result = authTokenDao.insert(AuthToken(response.pk, response.token))
                        if (result < 0) {
                            emit(
                                DataState.ERROR(
                                    Response(
                                        R.string.error_something_went_wrong,
                                        ResponseView.DIALOG()
                                    )
                                )
                            )
                        } else {
                            saveAuthUserToPrefs(response.email)
                            emit(
                                DataState.SUCCESS(
                                    AuthViewState(
                                        authToken = AuthToken(
                                            account_pk = response.pk,
                                            token = response.token
                                        )
                                    )
                                )
                            )
                        }
                    }

                    override suspend fun createCacheRequest() {

                    }

                }
            emitAll(networkBoundResource.call())
        }
    }

    fun checkPreviousAuthUser(): Flow<DataState<AuthViewState>> = flow {
        val previousAuthUserEmail = sharedPreferences.getString(PREVIOUS_AUTH_USER, null)
        previousAuthUserEmail?.let {
            val networkBoundResource = object : NetworkBoundResource<BaseResponse, AuthViewState>(
                errorHandler,
                sessionManager.isConnectedToInternet(),
                false
            ) {

                override suspend fun createCacheRequest() {
                    accountDao.searchByEmail(previousAuthUserEmail)?.let { account ->
                        if (account.pk > -1) {
                            authTokenDao.searchByPk(account.pk)?.let { authToken ->
                                emit(DataState.SUCCESS(AuthViewState(authToken = authToken)))
                            }
                        }
                    }
                }

                override suspend fun createNetworkRequest(): BaseResponse? {
                    return null
                }

                override suspend fun handleSuccess(response: BaseResponse) {

                }

            }
            emitAll(networkBoundResource.call())
        }
    }

    private fun saveAuthUserToPrefs(email: String) {
        sharedPrefsEditor.putString(PREVIOUS_AUTH_USER, email)
        sharedPrefsEditor.apply()
    }
}
