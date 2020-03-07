package com.mi.mvi.domain.auth

import androidx.lifecycle.LiveData
import com.mi.mvi.data.repository.auth.AuthRepository
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.ui.auth.state.AuthViewState

class CheckTokenUseCase(private val repository: AuthRepository) {

    fun invoke(): LiveData<DataState<AuthViewState>> {
        return repository.checkPreviousAuthUser()
    }
}