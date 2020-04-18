package com.mi.mvi.presentation.main.account

import androidx.lifecycle.LiveData
import com.mi.mvi.cache.entity.UserEntity
import com.mi.mvi.domain.usecase.account.ChangePasswordUseCase
import com.mi.mvi.domain.usecase.account.GetAccountUseCase
import com.mi.mvi.domain.usecase.account.UpdateAccountUseCase
import com.mi.mvi.presentation.base.BaseViewModel
import com.mi.mvi.presentation.main.account.state.AccountEventState
import com.mi.mvi.presentation.main.account.state.AccountEventState.*
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.utils.AbsentLiveData
import com.mi.mvi.utils.SessionManager
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AccountViewModel(
    private val sessionManager: SessionManager,
    private val accountUseCase: GetAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
) : BaseViewModel<AccountEventState, AccountViewState>() {

    override fun handleEventState(eventState: AccountEventState): LiveData<DataState<AccountViewState>> {
        return when (eventState) {
            is GetAccountEvent -> {
                sessionManager.cachedTokenEntity.value?.let { accountUseCase.invoke(it) }
                    ?: AbsentLiveData.create()
            }
            is UpdateAccountEvent -> {
                sessionManager.cachedTokenEntity.value?.let { authToken ->
                    authToken.account_pk?.let { pk ->
                        updateAccountUseCase.invoke(
                            authToken,
                            UserEntity(
                                pk,
                                eventState.email,
                                eventState.username
                            )
                        )
                    }

                } ?: AbsentLiveData.create()
            }
            is ChangePasswordEvent -> {
                sessionManager.cachedTokenEntity.value?.let { authToken ->
                    changePasswordUseCase.invoke(
                        authToken,
                        eventState.currentPassword,
                        eventState.newPassword,
                        eventState.confirmNewPassword
                    )
                }
                    ?: AbsentLiveData.create()
            }
            is None -> {
                object : LiveData<DataState<AccountViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        value = DataState.LOADING(false)
                    }
                }
            }
        }
    }

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    fun setAccountData(userEntity: UserEntity) {
        val update = getCurrentViewStateOrNew()
        if (update.userEntity != userEntity) {
            update.userEntity = userEntity
            _viewState.value = update
        }
    }

    fun logout() {
        sessionManager.logout()
    }
}