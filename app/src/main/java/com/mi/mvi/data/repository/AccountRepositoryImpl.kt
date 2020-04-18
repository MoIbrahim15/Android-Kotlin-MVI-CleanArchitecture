package com.mi.mvi.data.repository

import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.remote.AccountRemoteDataSource
import com.mi.mvi.datasource.model.AccountProperties
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.datasource.model.BaseResponse
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.utils.ErrorHandling.Companion.SOMETHING_WRONG_WITH_IMAGE
import com.mi.mvi.utils.SuccessHandling.Companion.RESPONSE_PASSWORD_UPDATE_SUCCESS
import com.mi.mvi.utils.SuccessHandling.Companion.SUCCESS
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.MessageType
import com.mi.mvi.utils.response_handler.StateMessage
import com.mi.mvi.utils.response_handler.UIComponentType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class AccountRepositoryImpl(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountCacheDataSource: AccountCacheDataSource
) : AccountRepository {

    override fun getAccountProperties(authToken: AuthToken): Flow<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
                IO,
                apiCall = { accountRemoteDataSource.getAccountProperties("Token ${authToken.token}") },
                cacheCall = { accountCacheDataSource.searchByPk(authToken.account_pk!!) }
            ) {
            override suspend fun handleCacheSuccess(response: AccountProperties?): DataState<AccountViewState>? {
                return DataState.LOADING(false, AccountViewState(response))
            }

            override suspend fun updateCache(networkObject: AccountProperties) {
                accountCacheDataSource.updateAccountProperties(
                    networkObject.pk,
                    networkObject.email,
                    networkObject.username
                )
            }

            override suspend fun handleNetworkSuccess(response: AccountProperties): DataState<AccountViewState>? {
                return DataState.SUCCESS(AccountViewState(response))
            }

        }.result
    }

    override fun updateAccountProperties(
        authToken: AuthToken,
        accountProperties: AccountProperties
    ): Flow<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<BaseResponse, Any, AccountViewState>(
                IO,
                apiCall = {
                    accountRemoteDataSource.updateAccountProperties(
                        "Token ${authToken.token}",
                        accountProperties.email,
                        accountProperties.username
                    )
                }
            ) {

            override suspend fun updateCache(networkObject: BaseResponse) {
                accountCacheDataSource.updateAccountProperties(
                    accountProperties.pk,
                    accountProperties.email,
                    accountProperties.username
                )
            }

            override suspend fun handleNetworkSuccess(response: BaseResponse): DataState<AccountViewState>? {
                return DataState.ERROR(
                    StateMessage("SUCCESS", UIComponentType.TOAST, MessageType.SUCCESS)
                )
            }

        }.result
    }

    override fun changePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<DataState<AccountViewState>> {
        return object : NetworkBoundResource<BaseResponse, Any, AccountViewState>(
            IO,
            apiCall = {
                accountRemoteDataSource.changePassword(
                    "Token ${authToken.token}",
                    currentPassword,
                    newPassword, confirmNewPassword
                )
            }
        ) {
            override suspend fun handleNetworkSuccess(response: BaseResponse): DataState<AccountViewState>? {

                return if (response.response == RESPONSE_PASSWORD_UPDATE_SUCCESS)
                    DataState.ERROR(
                        StateMessage(SUCCESS, UIComponentType.TOAST, MessageType.SUCCESS)
                    )
                else {
                    DataState.ERROR(
                        StateMessage(
                            SOMETHING_WRONG_WITH_IMAGE,
                            UIComponentType.TOAST,
                            MessageType.ERROR
                        )
                    )
                }
            }
        }.result
    }
}