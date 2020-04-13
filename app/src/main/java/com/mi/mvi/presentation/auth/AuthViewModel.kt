package com.mi.mvi.presentation.auth

import androidx.lifecycle.LiveData
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.domain.usecase.auth.CheckTokenUseCase
import com.mi.mvi.domain.usecase.auth.LoginUseCase
import com.mi.mvi.domain.usecase.auth.RegisterUseCase
import com.mi.mvi.presentation.BaseViewModel
import com.mi.mvi.presentation.auth.state.AuthEventState
import com.mi.mvi.presentation.auth.state.AuthEventState.*
import com.mi.mvi.presentation.auth.state.AuthViewState
import com.mi.mvi.presentation.auth.state.LoginFields
import com.mi.mvi.presentation.auth.state.RegistrationFields
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val checkTokenUseCase: CheckTokenUseCase
) : BaseViewModel<AuthEventState, AuthViewState>() {

    override fun handleEventState(eventState: AuthEventState): LiveData<DataState<AuthViewState>> {
        return when (eventState) {
            is LoginEvent -> {
                loginUseCase.invoke(eventState.email, eventState.password)
            }
            is RegisterEvent -> {
                registerUseCase.invoke(
                    eventState.email,
                    eventState.username,
                    eventState.password,
                    eventState.confirmPassword
                )
            }
            is CheckTokenEvent -> {
                checkTokenUseCase.invoke()
            }
            is None -> {
                object : LiveData<DataState<AuthViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        value = DataState.LOADING(false)
                    }
                }
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