package com.mi.mvi.domain.usecase.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mi.mvi.domain.repository.AuthRepository
import com.mi.mvi.presentation.auth.state.AuthViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class RegisterUseCase(private val repository: AuthRepository) {
    fun invoke(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {
        return repository.register(email, username, password, confirmPassword).flowOn(IO)
            .asLiveData()
    }
}