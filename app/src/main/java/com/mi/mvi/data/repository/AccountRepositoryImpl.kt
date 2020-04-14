package com.mi.mvi.data.repository

import com.mi.mvi.R
import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.remote.AccountRemoteDataSource
import com.mi.mvi.datasource.model.AccountProperties
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.datasource.model.BaseResponse
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.utils.SessionManager
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.ErrorConstants.Companion.RESPONSE_PASSWORD_UPDATE_SUCCESS
import com.mi.mvi.utils.response_handler.ErrorHandler
import com.mi.mvi.utils.response_handler.Response
import com.mi.mvi.utils.response_handler.ResponseView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class AccountRepositoryImpl(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountCacheDataSource: AccountCacheDataSource,
    private val sessionManager: SessionManager,
    private val errorHandler: ErrorHandler
) : AccountRepository {

    override fun getAccountProperties(authToken: AuthToken): Flow<DataState<AccountViewState>> =
        flow {
            val networkBoundResource =
                object :
                    NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
                        apiCall = { accountRemoteDataSource.getAccountProperties("Token ${authToken.token}") },
                        cacheCall = { accountCacheDataSource.searchByPk(authToken.account_pk!!) },
                        errorHandler = errorHandler,
                        isNetworkAvailable = sessionManager.isConnectedToInternet(),
                        canWorksOffline = true
                    ) {
                    override suspend fun handleCacheSuccess(response: AccountProperties?) {
                        emit(DataState.LOADING(false, AccountViewState(response)))
                    }

                    override suspend fun handleNetworkSuccess(response: AccountProperties) {
                        emit(DataState.SUCCESS(AccountViewState(response)))
                        accountCacheDataSource.updateAccountProperties(
                            response.pk,
                            response.email,
                            response.username
                        )
                    }

                }
            emitAll(networkBoundResource.call())
        }

    override fun updateAccountProperties(
        authToken: AuthToken,
        accountProperties: AccountProperties
    ): Flow<DataState<AccountViewState>> = flow {
        val networkBoundResource = object :
            NetworkBoundResource<BaseResponse, Any, AccountViewState>(
                apiCall = {
                    accountRemoteDataSource.updateAccountProperties(
                        "Token ${authToken.token}",
                        accountProperties.email,
                        accountProperties.username
                    )
                },
                cacheCall = null,
                errorHandler = errorHandler,
                isNetworkAvailable = sessionManager.isConnectedToInternet(),
                canWorksOffline = false
            ) {

            override suspend fun handleNetworkSuccess(response: BaseResponse) {
                accountCacheDataSource.updateAccountProperties(
                    accountProperties.pk,
                    accountProperties.email,
                    accountProperties.username
                )
                emit(
                    DataState.SUCCESS<AccountViewState>(
                        data = null,
                        response = Response(R.string.text_success, ResponseView.TOAST())
                    )
                )

            }

            override suspend fun handleCacheSuccess(response: Any?) {

            }
        }
        emitAll(networkBoundResource.call())
    }

    override fun changePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<DataState<AccountViewState>> = flow {
        val networkBoundResource =
            object : NetworkBoundResource<BaseResponse, Any, AccountViewState>(
                apiCall = {
                    accountRemoteDataSource.changePassword(
                        "Token ${authToken.token}",
                        currentPassword,
                        newPassword, confirmNewPassword
                    )
                },
                cacheCall = null,
                errorHandler = errorHandler,
                isNetworkAvailable = sessionManager.isConnectedToInternet(),
                canWorksOffline = false
            ) {
                override suspend fun handleNetworkSuccess(response: BaseResponse) {

                    if (response.response == RESPONSE_PASSWORD_UPDATE_SUCCESS)
                        emit(
                            DataState.SUCCESS<AccountViewState>(
                                data = null,
                                response = Response(R.string.text_success, ResponseView.TOAST())
                            )
                        )
                    else {
                        emit(
                            DataState.ERROR<AccountViewState>(
                                response = Response(
                                    R.string.error_something_went_wrong,
                                    ResponseView.TOAST()
                                )
                            )
                        )
                    }
                }

                override suspend fun handleCacheSuccess(response: Any?) {

                }

            }

        emitAll(networkBoundResource.call())
    }
}