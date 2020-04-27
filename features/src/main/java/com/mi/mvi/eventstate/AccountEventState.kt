package com.mi.mvi.eventstate

sealed class AccountEventState {

    object GetAccountEvent : AccountEventState()

    data class UpdateAccountEvent(
        val email: String,
        val username: String
    ) : AccountEventState()

    data class ChangePasswordEvent(
        val currentPassword: String,
        val newPassword: String,
        val confirmNewPassword: String
    ) : AccountEventState()

    object None : AccountEventState()
}
