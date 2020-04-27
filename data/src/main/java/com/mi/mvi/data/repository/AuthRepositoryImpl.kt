package com.mi.mvi.data.repository

import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.cache.TokenCacheDataSource
import com.mi.mvi.data.datasource.remote.AuthRemoteDataSource
import com.mi.mvi.data.entity.BaseEntity
import com.mi.mvi.data.entity.TokenEntity
import com.mi.mvi.data.entity.UserEntity
import com.mi.mvi.data.mapper.TokenMapper
import com.mi.mvi.domain.Constants.Companion.GENERIC_AUTH_ERROR
import com.mi.mvi.domain.Constants.Companion.UNKNOWN_ERROR
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.datastate.MessageType
import com.mi.mvi.domain.datastate.StateMessage
import com.mi.mvi.domain.datastate.UIComponentType
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.repository.AuthRepository
import com.mi.mvi.domain.viewstate.AuthViewState
import com.mi.mvi.domain.viewstate.LoginFields
import com.mi.mvi.domain.viewstate.RegistrationFields
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


@ExperimentalCoroutinesApi
class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val tokenCacheDataSource: TokenCacheDataSource,
    private val accountCacheDataSource: AccountCacheDataSource,
    private val tokenMapper: TokenMapper
) : AuthRepository {

    override fun login(
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>> = flow {
        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if (loginFieldErrors == LoginFields.LoginError.none()) {
            emitAll(object : NetworkBoundResource<UserEntity, UserEntity, AuthViewState>(
                IO,
                apiCall = { authRemoteDataSource.login(email, password) }
            ) {

                override suspend fun handleNetworkSuccess(response: UserEntity): DataState<AuthViewState>? {
                    if (response.response != GENERIC_AUTH_ERROR) {
                        accountCacheDataSource.insertOrIgnore(
                            UserEntity(
                                response.pk,
                                response.email,
                                ""
                            )
                        )
                        val result = tokenCacheDataSource.insert(
                            TokenEntity(
                                response.pk,
                                response.token
                            )
                        )
                        return if (result < 0) {
                            buildDialogError(UNKNOWN_ERROR)
                        } else {
                            accountCacheDataSource. saveLoggedInEmail(response.email)
                            DataState.SUCCESS(
                                AuthViewState(
                                    token = Token(
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
                NetworkBoundResource<UserEntity, UserEntity, AuthViewState>(
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

                override suspend fun handleNetworkSuccess(response: UserEntity): DataState<AuthViewState>? {
                    accountCacheDataSource.insertOrIgnore(
                        UserEntity(
                            response.pk,
                            response.email,
                            ""
                        )
                    )
                    val result = tokenCacheDataSource.insert(
                        TokenEntity(
                            response.pk,
                            response.token
                        )
                    )
                    return if (result < 0) {
                        buildDialogError(UNKNOWN_ERROR)
                    } else {
                        accountCacheDataSource. saveLoggedInEmail(response.email)
                        DataState.SUCCESS(
                            AuthViewState(
                                token = Token(
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
        val loggedInEmail = accountCacheDataSource.getLoggedInEmail()
        if (!loggedInEmail.isNullOrBlank()) {
            emitAll(object : NetworkBoundResource<BaseEntity, UserEntity, AuthViewState>(
                IO,
                cacheCall = { accountCacheDataSource.searchByEmail(loggedInEmail) }
            ) {
                override suspend fun handleCacheSuccess(response: UserEntity?): DataState<AuthViewState>? {
                    response?.let { account ->
                        account.pk?.let {
                            if (it > -1) {
                                tokenCacheDataSource.searchTokenByPk(it)?.let { authToken ->
                                    return DataState.SUCCESS(
                                        AuthViewState(
                                            token = tokenMapper.mapFromEntity(
                                                authToken
                                            )
                                        )
                                    )
                                }
                            } else {
                                return buildDialogError(UNKNOWN_ERROR)
                            }
                        }
                    } ?: return buildDialogError(UNKNOWN_ERROR)
                }
            }.result)
        } else {
            emit(
                DataState.ERROR<AuthViewState>(
                    StateMessage(
                        message = "Logged Out user",
                        uiComponentType = UIComponentType.NONE,
                        messageType = MessageType.ERROR
                    )
                )
            )
        }
    }

    override suspend fun nullifyToken(pk: Int) {
        tokenCacheDataSource.nullifyToken(pk)
    }
}
