package com.mi.mvi.ui.main.account

import androidx.lifecycle.LiveData
import com.mi.mvi.data.models.AccountProperties
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.session.SessionManager
import com.mi.mvi.domain.main.account.ChangePasswordUseCase
import com.mi.mvi.domain.main.account.GetAccountUseCase
import com.mi.mvi.domain.main.account.UpdateAccountUseCase
import com.mi.mvi.ui.BaseViewModel
import com.mi.mvi.ui.main.account.state.AccountEventState
import com.mi.mvi.ui.main.account.state.AccountEventState.*
import com.mi.mvi.ui.main.account.state.AccountViewState
import com.mi.mvi.utils.AbsentLiveData
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
                sessionManager.cachedToken.value?.let { accountUseCase.invoke(it) }?:AbsentLiveData.create()
            }
            is ChangePasswordEvent -> {
                AbsentLiveData.create()

            }
            is UpdateAccountEvent -> {
                AbsentLiveData.create()
            }
            is None -> {
                AbsentLiveData.create()
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