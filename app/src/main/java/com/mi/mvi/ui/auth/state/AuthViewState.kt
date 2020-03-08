package com.mi.mvi.ui.auth.state

import com.mi.mvi.R
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.response_handler.ErrorConstants.Companion.SUCCESS_CODE


data class AuthViewState(
    var registrationFields: RegistrationFields? = RegistrationFields(),
    var loginFields: LoginFields? = LoginFields(),
    var authToken: AuthToken? = null
)

data class RegistrationFields(
    var email: String? = null,
    var username: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null
) {
    fun registerError(): Int {
        return if (email.isNullOrEmpty()
            || username.isNullOrEmpty()
            || password.isNullOrEmpty()
            || confirmPassword.isNullOrEmpty()
        ) {
            R.string.error_all_fields_are_required
        } else if (!password.equals(confirmPassword)) {
            R.string.error_passwords_must_same
        } else {
            SUCCESS_CODE
        }
    }
}

data class LoginFields(
    var email: String? = null,
    var password: String? = null
) {
    fun loginError(): Int {
        return if (email.isNullOrEmpty()
            || password.isNullOrEmpty()
        ) {
            R.string.error_all_fields_are_required
        } else SUCCESS_CODE
    }
}