package com.mi.mvi.ui.auth

import androidx.lifecycle.LiveData
import com.mi.mvi.base.BaseViewModel
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.network.responses.LoginResponse
import com.mi.mvi.domain.auth.ForgetUseCase
import com.mi.mvi.domain.auth.LoginUseCase
import com.mi.mvi.domain.auth.RegisterUseCase
import com.mi.mvi.ui.auth.state.AuthEventState
import com.mi.mvi.ui.auth.state.AuthEventState.*
import com.mi.mvi.ui.auth.state.AuthViewState
import com.mi.mvi.ui.auth.state.LoginFields
import com.mi.mvi.ui.auth.state.RegistrationFields
import com.mi.mvi.utils.AbsentLiveData
import com.mi.mvi.data.response_handler.DataState

class AuthViewModel(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val forgetUseCase: ForgetUseCase
) : BaseViewModel<AuthEventState, AuthViewState>() {

//    private val _loginLiveData: MutableLiveData<LoginResponse> = MutableLiveData()
//     val login: LiveData<LoginResponse>
//        get() = _loginLiveData

    fun login(email: String, password: String): LiveData<DataState<LoginResponse>> {
        return loginUseCase.invoke(email, password)
    }

    override fun handleEventState(eventState: AuthEventState): LiveData<DataState<AuthViewState>> {
        return when (eventState) {
            is LoginEvent -> {
                AbsentLiveData.create()
            }
            is RegisterEvent -> {
                AbsentLiveData.create()
            }
            is CheckTokenEvent -> {
                AbsentLiveData.create()
            }
        }
    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        if (update.registrationFields != registrationFields)
            update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        if (update.loginFields != loginFields)
            update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken != authToken)
            update.authToken = authToken
        _viewState.value = update
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

}