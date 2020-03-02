package com.mi.mvi.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mi.mvi.data.network.responses.LoginResponse
import com.mi.mvi.domain.auth.ForgetUseCase
import com.mi.mvi.domain.auth.LoginUseCase
import com.mi.mvi.domain.auth.RegisterUseCase
import com.mi.mvi.utils.Resource

class AuthViewModel(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val forgetUseCase: ForgetUseCase
) : ViewModel() {

//    private val _loginLiveData: MutableLiveData<LoginResponse> = MutableLiveData()
//     val login: LiveData<LoginResponse>
//        get() = _loginLiveData

    fun login(email : String, password: String) :LiveData<Resource<LoginResponse>> {
       return loginUseCase.invoke(email,password)
    }

}