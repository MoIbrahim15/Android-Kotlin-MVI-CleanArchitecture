package com.mi.mvi.data.repository

import android.content.SharedPreferences
import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.cache.AuthCacheDataSource
import com.mi.mvi.data.datasource.remote.AuthRemoteDataSource
import com.mi.mvi.datasource.model.*
import com.mi.mvi.domain.repository.AuthRepository
import com.mi.mvi.presentation.auth.state.AuthViewState
import com.mi.mvi.presentation.auth.state.LoginFields
import com.mi.mvi.presentation.auth.state.RegistrationFields
import com.mi.mvi.utils.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.mi.mvi.utils.ErrorHandling.Companion.UNKNOWN_ERROR
import com.mi.mvi.utils.SharedPreferenceKeys.Companion.PREVIOUS_AUTH_USER
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.MessageType
import com.mi.mvi.utils.response_handler.StateMessage
import com.mi.mvi.utils.response_handler.UIComponentType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


@ExperimentalCoroutinesApi
class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authCacheDataSource: AuthCacheDataSource,
    private val accountCacheDataSource: AccountCacheDataSource,
    private val sharedPreferences: SharedPreferences,
    private val sharedPrefsEditor: SharedPreferences.Editor
) : AuthRepository {

    override fun login(
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>> = flow {
        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if (loginFieldErrors == LoginFields.LoginError.none()) {
            emitAll(object : NetworkBoundResource<LoginResponse, LoginResponse, AuthViewState>(
                IO,
                apiCall = { authRemoteDataSource.login(email, password) }
            ) {

                override suspend fun handleNetworkSuccess(response: LoginResponse): DataState<AuthViewState>? {
                    if (response.response != GENERIC_AUTH_ERROR) {
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
                        return if (result < 0) {
                            buildDialogError(UNKNOWN_ERROR)
                        } else {
                            saveAuthUserToPrefs(response.email)
                            DataState.SUCCESS(
                                AuthViewState(
                                    authToken = AuthToken(
                                        account_pk = response.pk,
                                        token = response.token
                                    )
                                )
                            )
                        }
                    } else {
                        return buildDialogError(response.errorMessage)
                    }
                }
            }.result)
        } else {
            emit(
                DataState.ERROR<AuthViewState>(
                    StateMessage(
                        message = loginFieldErrors,
                        uiComponentType = UIComponentType.DIALOG,
                        messageType = MessageType.ERROR
                    )
                )
            )
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
            emitAll(object :
                NetworkBoundResource<RegisterResponse, RegisterResponse, AuthViewState>(
                    IO,
                    apiCall = {
                        authRemoteDataSource.register(
                            email,
                            username,
                            password,
                            confirmPassword
                        )
                    }
                ) {

                override suspend fun handleNetworkSuccess(response: RegisterResponse): DataState<AuthViewState>? {
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
                    return if (result < 0) {
                        buildDialogError(UNKNOWN_ERROR)
                    } else {
                        saveAuthUserToPrefs(response.email)
                        DataState.SUCCESS(
                            AuthViewState(
                                authToken = AuthToken(
                                    account_pk = response.pk,
                                    token = response.token
                                )
                            )
                        )
                    }
                }
            }.result)
        } else {
            emit(
                DataState.ERROR<AuthViewState>(
                    StateMessage(
                        message = registrationFieldErrors,
                        uiComponentType = UIComponentType.DIALOG,
                        messageType = MessageType.ERROR
                    )
                )
            )
        }
    }

    override fun checkPreviousAuthUser(): Flow<DataState<AuthViewState>> = flow {
        val previousAuthUserEmail = sharedPreferences.getString(PREVIOUS_AUTH_USER, null)
        if (!previousAuthUserEmail.isNullOrBlank()) {
            emitAll(object : NetworkBoundResource<BaseResponse, AccountProperties, AuthViewState>(
                IO,
                cacheCall = { accountCacheDataSource.searchByEmail(previousAuthUserEmail) }
            ) {
                override suspend fun handleCacheSuccess(response: AccountProperties?): DataState<AuthViewState>? {
                    response?.let { account ->
                        if (account.pk > -1) {
                            authCacheDataSource.searchTokenByPk(account.pk)?.let { authToken ->
                                return DataState.SUCCESS(AuthViewState(authToken = authToken))
                            }
                        } else {
                            return buildDialogError(UNKNOWN_ERROR)
                        }
                    } ?: return buildDialogError(UNKNOWN_ERROR)
                }
            }.result)
        }
    }

    override fun saveAuthUserToPrefs(email: String) {
        sharedPrefsEditor.putString(PREVIOUS_AUTH_USER, email)
        sharedPrefsEditor.apply()
    }
}
