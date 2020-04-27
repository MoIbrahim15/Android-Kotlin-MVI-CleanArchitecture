package com.mi.mvi.domain.usecase.account

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.model.User
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.domain.viewstate.AccountViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class UpdateAccountUseCase(private val repository: AccountRepository) {

    fun invoke(
        token: Token,
        user: User
    ): Flow<DataState<AccountViewState>> {
        return repository.updateAccountProperties(token, user)
            .flowOn(Dispatchers.IO)
    }
}
