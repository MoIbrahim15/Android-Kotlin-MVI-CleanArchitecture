package com.mi.mvi.ui.auth.state

sealed class AuthEventState {

    data class LoginEvent(
        val email: String,
        val password: String
    ) : AuthEventState()

    data class RegisterEvent(
        val email: String,
        val username: String,
        val password: String,
        val confirmPassword: String
    ) : AuthEventState()


    class CheckTokenEvent : AuthEventState()

    class None : AuthEventState()

}