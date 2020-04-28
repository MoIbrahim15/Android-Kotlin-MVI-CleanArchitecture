package com.mi.mvi.events

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

    object CheckTokenEvent : AuthEventState()

    object None : AuthEventState()
}
