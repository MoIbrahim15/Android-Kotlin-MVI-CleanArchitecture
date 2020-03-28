package com.mi.mvi.data.repository.main

import com.mi.mvi.data.database.AccountDao
import com.mi.mvi.data.models.AccountProperties
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.network.main.MainApiService
import com.mi.mvi.data.network.responses.BaseResponse
import com.mi.mvi.data.repository.BaseRepository
import com.mi.mvi.data.repository.NetworkBoundResource
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.response_handler.ErrorHandler
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.ui.main.account.state.AccountViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class AccountRepository(
    private val apiService: MainApiService,
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
    private val errorHandler: ErrorHandler
) : BaseRepository() {

    fun getAccountProperties(authToken: AuthToken): Flow<DataState<AccountViewState>> = flow {
        val networkBoundResource =
            object : NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
                apiCall = { apiService.getAccountProperties("Token ${authToken.token}") },
                cacheCall = { accountDao.searchByPk(authToken.account_pk!!) },
                errorHandler = errorHandler,
                isNetworkAvailable = sessionManager.isConnectedToInternet(),
                canWorksOffline = true
            ) {
                override suspend fun handleCacheSuccess(response: AccountProperties?) {
                    emit(DataState.LOADING(false, AccountViewState(response)))
                }

                override suspend fun handleNetworkSuccess(response: AccountProperties) {
                    emit(DataState.SUCCESS(AccountViewState(response)))
                    accountDao.updateAccountProperties(
                        response.pk,
                        response.email,
                        response.username
                    )
                }

            }
        emitAll(networkBoundResource.call())
    }

    fun updateAccountProperties(
        authToken: AuthToken,
        accountProperties: AccountProperties
    ): Flow<DataState<AccountViewState>> = flow {
        val networkBoundResource = object :
            NetworkBoundResource<BaseResponse, Any, AccountViewState>(
                apiCall = {
                    apiService.updateAccountProperties(
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
                accountDao.updateAccountProperties(
                    accountProperties.pk,
                    accountProperties.email,
                    accountProperties.username
                )

                emit(DataState.LOADING(isLoading = false, cachedData = null))
            }

            override suspend fun handleCacheSuccess(response: Any?) {

            }
        }
        emitAll(networkBoundResource.call())
    }
}