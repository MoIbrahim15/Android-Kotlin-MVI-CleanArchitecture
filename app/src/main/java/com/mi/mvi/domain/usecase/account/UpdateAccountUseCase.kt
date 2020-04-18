package com.mi.mvi.domain.usecase.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mi.mvi.cache.entity.AuthTokenEntity
import com.mi.mvi.cache.entity.UserEntity
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.presentation.main.account.state.AccountViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class UpdateAccountUseCase(private val repository: AccountRepository) {

    fun invoke(
        tokenEntity: AuthTokenEntity,
        userEntity: UserEntity
    ): LiveData<DataState<AccountViewState>> {
        return repository.updateAccountProperties(tokenEntity, userEntity)
            .flowOn(Dispatchers.IO)
            .asLiveData()
    }
}