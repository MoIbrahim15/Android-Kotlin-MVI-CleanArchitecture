package com.mi.mvi.domain.auth

import androidx.lifecycle.LiveData
import com.mi.mvi.data.network.responses.LoginResponse
import com.mi.mvi.data.repository.auth.AuthRepository
import com.mi.mvi.utils.Resource

class LoginUseCase(val repository: AuthRepository) {

     fun invoke(email: String, password: String) :LiveData<Resource<LoginResponse>> {
        return repository.login(email, password)
     }
}