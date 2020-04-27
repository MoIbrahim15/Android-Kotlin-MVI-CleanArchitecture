package com.mi.mvi.features.auth

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.usecase.auth.CheckTokenUseCase
import com.mi.mvi.domain.usecase.auth.LoginUseCase
import com.mi.mvi.domain.usecase.auth.RegisterUseCase
import com.mi.mvi.domain.viewstate.AuthViewState
import com.mi.mvi.domain.viewstate.LoginFields
import com.mi.mvi.domain.viewstate.RegistrationFields
import com.mi.mvi.eventstate.AuthEventState
import com.mi.mvi.eventstate.AuthEventState.*
import com.mi.mvi.features.base.BaseViewModel
import com.mi.mvi.mapper.TokenMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@FlowPreview
@ExperimentalCoroutinesApi
class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val checkTokenUseCase: CheckTokenUseCase,
    private val tokenMapper: TokenMapper
) : BaseViewModel<AuthEventState, AuthViewState>() {

    override fun handleEventState(eventState: AuthEventState): Flow<DataState<AuthViewState>> =
        flow {
            when (eventState) {
                is LoginEvent -> {
                    emitAll(loginUseCase.invoke(eventState.email, eventState.password))
                }
                is RegisterEvent -> {
                    emitAll(
                        registerUseCase.invoke(
                            eventState.email,
                            eventState.username,
                            eventState.password,
                            eventState.confirmPassword
                        )
                    )
                }
                is CheckTokenEvent -> {
                    emitAll(checkTokenUseCase.invoke())
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

    fun setAuthToken(token: Token) {
        val update = getCurrentViewStateOrNew()
        if (update.token != token)
            update.token = token
        _viewState.value = update
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }
}
