package com.mi.mvi.domain.auth

import androidx.lifecycle.LiveData
import com.mi.mvi.data.repository.auth.AuthRepository
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.ui.auth.state.AuthViewState

class LoginUseCase(private val repository: AuthRepository) {

    fun invoke(email: String, password: String): LiveData<DataState<AuthViewState>> {
        return repository.login(email, password)
    }
}