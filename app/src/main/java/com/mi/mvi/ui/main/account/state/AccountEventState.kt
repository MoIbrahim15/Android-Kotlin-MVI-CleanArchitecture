package com.mi.mvi.ui.main.account.state

sealed class AccountEventState {

    class GetAccountEvent : AccountEventState()

    data class UpdateAccountEvent(
        val email: String,
        val username: String
    ) : AccountEventState()

    data class ChangePasswordEvent(
        val currentPassword: String,
        val newPassword: String,
        val confirmNewPassword: String
    ) : AccountEventState()

    class None(): AccountEventState()
}