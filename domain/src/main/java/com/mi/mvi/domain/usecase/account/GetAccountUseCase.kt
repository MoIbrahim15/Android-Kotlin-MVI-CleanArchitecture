package com.mi.mvi.domain.usecase.account

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.domain.viewstate.AccountViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class GetAccountUseCase(private val repository: AccountRepository) {

    fun invoke(tokenEntity: Token): Flow<DataState<AccountViewState>> {
        return repository.getAccountProperties(tokenEntity).flowOn(Dispatchers.IO)
    }
}
