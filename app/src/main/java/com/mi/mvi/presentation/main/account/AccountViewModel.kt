package com.mi.mvi.presentation.main.account

import androidx.lifecycle.LiveData
import com.mi.mvi.datasource.model.AccountProperties
import com.mi.mvi.domain.usecase.account.ChangePasswordUseCase
import com.mi.mvi.domain.usecase.account.GetAccountUseCase
import com.mi.mvi.domain.usecase.account.UpdateAccountUseCase
import com.mi.mvi.presentation.BaseViewModel
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
                sessionManager.cachedToken.value?.let { accountUseCase.invoke(it) }
                    ?: AbsentLiveData.create()
            }
            is UpdateAccountEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    authToken.account_pk?.let { pk ->
                        updateAccountUseCase.invoke(
                            authToken,
                            AccountProperties(
                                pk,
                                eventState.email,
                                eventState.username
                            )
                        )
                    }

                } ?: AbsentLiveData.create()
            }
            is ChangePasswordEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
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

    fun setAccountData(accountProperties: AccountProperties) {
        val update = getCurrentViewStateOrNew()
        if (update.accountProperties != accountProperties) {
            update.accountProperties = accountProperties
            _viewState.value = update
        }
    }

    fun logout() {
        sessionManager.logout()
    }
}