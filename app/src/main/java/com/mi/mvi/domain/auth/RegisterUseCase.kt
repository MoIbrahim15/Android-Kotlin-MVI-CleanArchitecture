package com.mi.mvi.domain.auth

import androidx.lifecycle.LiveData
import com.mi.mvi.data.repository.auth.AuthRepository
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.ui.auth.state.AuthViewState

class RegisterUseCase(private val repository: AuthRepository) {
    fun invoke(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {
        return repository.register(email, username, password, confirmPassword)
    }
}