package com.mi.mvi.ui.main.account

import androidx.lifecycle.LiveData
import com.mi.mvi.data.models.Account
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

class AccountViewModel(
    val sessionManager: SessionManager,
    val accountUseCase: GetAccountUseCase,
    val updateAccountUseCase: UpdateAccountUseCase,
    val changePasswordUseCase: ChangePasswordUseCase
) : BaseViewModel<AccountEventState, AccountViewState>() {

    override fun handleEventState(eventState: AccountEventState): LiveData<DataState<AccountViewState>> {
        return when (eventState) {
            is GetAccountEvent -> {
                AbsentLiveData.create()
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

    fun setAccountData(account: Account) {
        val update = getCurrentViewStateOrNew()
        if (update.account != account) {
            update.account = account
            _viewState.value = update
        }
    }

    fun logout(){
        sessionManager.logout()
    }
}