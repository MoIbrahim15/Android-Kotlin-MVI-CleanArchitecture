package com.mi.mvi.domain.usecase.auth

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.repository.AuthRepository
import com.mi.mvi.domain.viewstate.AuthViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class RegisterUseCase(private val repository: AuthRepository) {
    fun invoke(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>> {
        return repository.register(email, username, password, confirmPassword).flowOn(IO)
    }
}
