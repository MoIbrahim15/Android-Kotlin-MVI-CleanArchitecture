package com.mi.mvi.data.repository

import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.remote.AccountRemoteDataSource
import com.mi.mvi.data.entity.BaseEntity
import com.mi.mvi.data.entity.UserEntity
import com.mi.mvi.data.mapper.UserMapper
import com.mi.mvi.domain.Constants.Companion.RESPONSE_PASSWORD_UPDATE_SUCCESS
import com.mi.mvi.domain.Constants.Companion.SOMETHING_WRONG_WITH_IMAGE
import com.mi.mvi.domain.Constants.Companion.SUCCESS
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.datastate.MessageType
import com.mi.mvi.domain.datastate.StateMessage
import com.mi.mvi.domain.datastate.UIComponentType
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.model.User
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.domain.viewstate.AccountViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class AccountRepositoryImpl(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountCacheDataSource: AccountCacheDataSource,
    private val userMapper: UserMapper
) : AccountRepository {

    override fun getAccountProperties(token: Token): Flow<DataState<AccountViewState>> {
       return object :
            NetworkBoundResource<UserEntity, UserEntity, AccountViewState>(
                IO,
                apiCall = { accountRemoteDataSource.getAccountProperties("Token ${token.token}") },
                cacheCall = { accountCacheDataSource.searchByPk(token.account_pk!!) }
            ) {
            override suspend fun handleCacheSuccess(response: UserEntity?): DataState<AccountViewState> {
                response?.let {
                    return DataState.LOADING(
                        true,
                        AccountViewState(userMapper.mapFromEntity(response))
                    )
                } ?: return DataState.LOADING(true, null)
            }

            override suspend fun updateCache(networkObject: UserEntity) {
                accountCacheDataSource.updateAccountProperties(
                    networkObject.pk,
                    networkObject.email,
                    networkObject.username
                )
            }

            override suspend fun handleNetworkSuccess(response: UserEntity): DataState<AccountViewState>? {
                return DataState.SUCCESS(AccountViewState(userMapper.mapFromEntity(response)))
            }
        }.result
    }

    override fun updateAccountProperties(
        token: Token,
        user: User
    ): Flow<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<BaseEntity, BaseEntity, AccountViewState>(
                IO,
                apiCall = {
                    accountRemoteDataSource.updateAccountProperties(
                        "Token ${token.token}",
                        user.email,
                        user.username
                    )
                }
            ) {

            override suspend fun updateCache(networkObject: BaseEntity) {
                accountCacheDataSource.updateAccountProperties(
                    user.pk,
                    user.email,
                    user.username
                )
            }

            override suspend fun handleNetworkSuccess(response: BaseEntity): DataState<AccountViewState>? {
                return DataState.ERROR(
                    StateMessage(
                        "SUCCESS",
                        UIComponentType.TOAST,
                        MessageType.SUCCESS
                    )
                )
            }
        }.result
    }

    override fun changePassword(
        token: Token,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<DataState<AccountViewState>> {
        return object : NetworkBoundResource<BaseEntity, BaseEntity, AccountViewState>(
            IO,
            apiCall = {
                accountRemoteDataSource.changePassword(
                    "Token ${token.token}",
                    currentPassword,
                    newPassword, confirmNewPassword
                )
            }
        ) {
            override suspend fun handleNetworkSuccess(response: BaseEntity): DataState<AccountViewState>? {

                return if (response.response == RESPONSE_PASSWORD_UPDATE_SUCCESS)
                    DataState.ERROR(
                        StateMessage(
                            SUCCESS,
                            UIComponentType.TOAST,
                            MessageType.SUCCESS
                        )
                    )
                else {
                    DataState.ERROR(
                        StateMessage(
                            SOMETHING_WRONG_WITH_IMAGE,
                            UIComponentType.DIALOG,
                            MessageType.ERROR
                        )
                    )
                }
            }
        }.result
    }
}
