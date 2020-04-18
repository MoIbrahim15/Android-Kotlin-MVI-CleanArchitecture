package com.mi.mvi.data.repository

import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.remote.AccountRemoteDataSource
import com.mi.mvi.cache.entity.UserEntity
import com.mi.mvi.cache.entity.AuthTokenEntity
import com.mi.mvi.remote.entity.BaseResponse
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.utils.Constants.Companion.RESPONSE_PASSWORD_UPDATE_SUCCESS
import com.mi.mvi.utils.Constants.Companion.SOMETHING_WRONG_WITH_IMAGE
import com.mi.mvi.utils.Constants.Companion.SUCCESS
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

    override fun getAccountProperties(authTokenEntity: AuthTokenEntity): Flow<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<UserEntity, UserEntity, AccountViewState>(
                IO,
                apiCall = { accountRemoteDataSource.getAccountProperties("Token ${authTokenEntity.token}") },
                cacheCall = { accountCacheDataSource.searchByPk(authTokenEntity.account_pk!!) }
            ) {
            override suspend fun handleCacheSuccess(response: UserEntity?): DataState<AccountViewState>? {
                return DataState.LOADING(false, AccountViewState(response))
            }

            override suspend fun updateCache(networkObject: UserEntity) {
                accountCacheDataSource.updateAccountProperties(
                    networkObject.pk,
                    networkObject.email,
                    networkObject.username
                )
            }

            override suspend fun handleNetworkSuccess(response: UserEntity): DataState<AccountViewState>? {
                return DataState.SUCCESS(AccountViewState(response))
            }

        }.result
    }

    override fun updateAccountProperties(
        authTokenEntity: AuthTokenEntity,
        userEntity: UserEntity
    ): Flow<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<BaseResponse, Any, AccountViewState>(
                IO,
                apiCall = {
                    accountRemoteDataSource.updateAccountProperties(
                        "Token ${authTokenEntity.token}",
                        userEntity.email,
                        userEntity.username
                    )
                }
            ) {

            override suspend fun updateCache(networkObject: BaseResponse) {
                accountCacheDataSource.updateAccountProperties(
                    userEntity.pk,
                    userEntity.email,
                    userEntity.username
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
        authTokenEntity: AuthTokenEntity,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<DataState<AccountViewState>> {
        return object : NetworkBoundResource<BaseResponse, Any, AccountViewState>(
            IO,
            apiCall = {
                accountRemoteDataSource.changePassword(
                    "Token ${authTokenEntity.token}",
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