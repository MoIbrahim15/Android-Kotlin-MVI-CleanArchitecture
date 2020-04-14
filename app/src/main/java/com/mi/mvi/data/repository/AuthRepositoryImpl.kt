package com.mi.mvi.data.repository

import android.content.SharedPreferences
import com.mi.mvi.R
import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.cache.AuthCacheDataSource
import com.mi.mvi.data.datasource.remote.AuthRemoteDataSource
import com.mi.mvi.datasource.model.*
import com.mi.mvi.domain.repository.AuthRepository
import com.mi.mvi.presentation.auth.state.AuthViewState
import com.mi.mvi.presentation.auth.state.LoginFields
import com.mi.mvi.presentation.auth.state.RegistrationFields
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.utils.SessionManager
import com.mi.mvi.utils.SharedPreferenceKeys.Companion.PREVIOUS_AUTH_USER
import com.mi.mvi.utils.response_handler.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


@ExperimentalCoroutinesApi
class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authCacheDataSource: AuthCacheDataSource,
    private val accountCacheDataSource: AccountCacheDataSource,
    private val sessionManager: SessionManager,
    private val errorHandler: ErrorHandler,
    private val sharedPreferences: SharedPreferences,
    private val sharedPrefsEditor: SharedPreferences.Editor
) : AuthRepository {

    override fun login(email: String, password: String): Flow<DataState<AuthViewState>> = flow {
        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if (loginFieldErrors == LoginFields.LoginError.none()) {
            val networkBoundResource =
                object : NetworkBoundResource<LoginResponse, LoginResponse, AuthViewState>(
                    apiCall = { authRemoteDataSource.login(email, password) },
                    cacheCall = null,
                    errorHandler = errorHandler,
                    isNetworkAvailable = sessionManager.isConnectedToInternet(),
                    canWorksOffline = false
                ) {

                    override suspend fun handleNetworkSuccess(response: LoginResponse) {
                        if (response.response != ErrorConstants.GENERIC_AUTH_ERROR) {
                            accountCacheDataSource.insertOrIgnore(
                                AccountProperties(
                                    response.pk,
                                    response.email,
                                    ""
                                )
                            )
                            val result = authCacheDataSource.insert(
                                AuthToken(
                                    response.pk,
                                    response.token
                                )
                            )
                            if (result < 0) {
                                emit(
                                    DataState.ERROR<AuthViewState>(
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
                            emit(errorHandler.invoke<AuthViewState>(message = response.errorMessage))
                        }
                    }

                    override suspend fun handleCacheSuccess(response: LoginResponse?) {

                    }
                }
            emitAll(networkBoundResource.call())
        } else {
            emit(DataState.ERROR<AuthViewState>(Response(loginFieldErrors, ResponseView.DIALOG())))
        }
    }


    override fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>> = flow {
        val registrationFieldErrors =
            RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if (registrationFieldErrors == RegistrationFields.RegistrationError.none()) {
            val networkBoundResource =
                object : NetworkBoundResource<RegisterResponse, RegisterResponse, AuthViewState>(
                    apiCall = { authRemoteDataSource.register(email, username, password, confirmPassword) },
                    cacheCall = null,
                    errorHandler = errorHandler,
                    isNetworkAvailable = sessionManager.isConnectedToInternet(),
                    canWorksOffline = false
                ) {


                    override suspend fun handleNetworkSuccess(response: RegisterResponse) {
                        accountCacheDataSource.insertOrIgnore(
                            AccountProperties(
                                response.pk,
                                response.email,
                                ""
                            )
                        )
                        val result = authCacheDataSource.insert(
                            AuthToken(
                                response.pk,
                                response.token
                            )
                        )
                        if (result < 0) {
                            emit(
                                DataState.ERROR<AuthViewState>(
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


                    override suspend fun handleCacheSuccess(response: RegisterResponse?) {

                    }

                }
            emitAll(networkBoundResource.call())
        } else {
            emit(DataState.ERROR<AuthViewState>(Response(registrationFieldErrors, ResponseView.DIALOG())))
        }
    }

    override fun checkPreviousAuthUser(): Flow<DataState<AuthViewState>> = flow {
        val previousAuthUserEmail = sharedPreferences.getString(PREVIOUS_AUTH_USER, null)
        if (previousAuthUserEmail != null) {
            val networkBoundResource =
                object : NetworkBoundResource<BaseResponse, AccountProperties, AuthViewState>(
                    apiCall = null,
                    cacheCall = { accountCacheDataSource.searchByEmail(previousAuthUserEmail) },
                    errorHandler = errorHandler,
                    isNetworkAvailable = sessionManager.isConnectedToInternet(),
                    canWorksOffline = false
                ) {
                    override suspend fun handleCacheSuccess(response: AccountProperties?) {
                        response?.let { account ->
                            if (account.pk > -1) {
                                authCacheDataSource.searchTokenByPk(account.pk)?.let { authToken ->
                                    emit(DataState.SUCCESS(AuthViewState(authToken = authToken)))
                                }
                            }
                        }
                    }

                    override suspend fun handleNetworkSuccess(response: BaseResponse) {

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
