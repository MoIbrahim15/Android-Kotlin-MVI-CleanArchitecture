package com.mi.mvi.features.auth

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.usecase.auth.CheckTokenUseCase
import com.mi.mvi.domain.usecase.auth.LoginUseCase
import com.mi.mvi.domain.usecase.auth.RegisterUseCase
import com.mi.mvi.domain.viewstate.AuthViewState
import com.mi.mvi.events.AuthEventState
import com.mi.mvi.events.AuthEventState.*
import com.mi.mvi.base.BaseViewModel
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
    private val checkTokenUseCase: CheckTokenUseCase) : BaseViewModel<AuthEventState, AuthViewState>() {

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

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }
}
