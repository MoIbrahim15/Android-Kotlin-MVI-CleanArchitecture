package com.mi.mvi.domain.usecase.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class ChangePasswordUseCase(private val repository: AccountRepository) {

    fun invoke(
        token: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): LiveData<DataState<AccountViewState>> {
        return repository.changePassword(token, currentPassword, newPassword, confirmNewPassword)
            .flowOn(Dispatchers.IO)
            .asLiveData()
    }
}