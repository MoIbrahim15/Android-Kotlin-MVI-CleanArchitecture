package com.mi.mvi.features.main.account

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.User
import com.mi.mvi.domain.usecase.account.ChangePasswordUseCase
import com.mi.mvi.domain.usecase.account.GetAccountUseCase
import com.mi.mvi.domain.usecase.account.UpdateAccountUseCase
import com.mi.mvi.domain.viewstate.AccountViewState
import com.mi.mvi.events.AccountEventState
import com.mi.mvi.events.AccountEventState.*
import com.mi.mvi.features.base.BaseViewModel
import com.mi.mvi.features.common.SessionManager
import com.mi.mvi.mapper.TokenMapper
import com.mi.mvi.mapper.UserMapper
import com.mi.mvi.model.UserView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@FlowPreview
@ExperimentalCoroutinesApi
class AccountViewModel(
    private val sessionManager: SessionManager,
    private val accountUseCase: GetAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val tokenMapper: TokenMapper,
    private val userMapper: UserMapper
) : BaseViewModel<AccountEventState, AccountViewState>() {

    override fun handleEventState(eventState: AccountEventState): Flow<DataState<AccountViewState>> =
        flow {
            when (eventState) {
                is GetAccountEvent -> {
                    sessionManager.cachedTokenViewEntity.value?.let {
                        emitAll(
                            accountUseCase.invoke(
                                tokenMapper.mapFromView(it)
                            )
                        )
                    }
                }
                is UpdateAccountEvent -> {
                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->
                        authToken.account_pk?.let { pk ->
                            emitAll(
                                updateAccountUseCase.invoke(
                                    tokenMapper.mapFromView(authToken),
                                    User(
                                        pk,
                                        eventState.email,
                                        eventState.username,
                                        null
                                    )
                                )
                            )
                        }
                    }
                }
                is ChangePasswordEvent -> {
                    sessionManager.cachedTokenViewEntity.value?.let { authToken ->
                        emitAll(
                            changePasswordUseCase.invoke(
                                tokenMapper.mapFromView(authToken),
                                eventState.currentPassword,
                                eventState.newPassword,
                                eventState.confirmNewPassword
                            )
                        )
                    }

                }
            }
        }

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    fun setAccountData(userEntity: UserView) {
        val update = getCurrentViewStateOrNew()
        if (update.userEntity != userMapper.mapFromView(userEntity)) {
            update.userEntity = userMapper.mapFromView(userEntity)
            _viewState.value = update
        }
    }

    fun logout() {
        sessionManager.logout()
    }
}
