package com.mi.mvi.domain.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mi.mvi.data.repository.auth.AuthRepository
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.ui.auth.state.AuthViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class CheckTokenUseCase (private val repository: AuthRepository) {

    fun invoke(): LiveData<DataState<AuthViewState>> {
        return repository.checkPreviousAuthUser().flowOn(IO).asLiveData()
    }
}