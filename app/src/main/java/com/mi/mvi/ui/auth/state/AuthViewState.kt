package com.mi.mvi.ui.auth.state

import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.response_handler.ResponseEntity
import com.mi.mvi.data.response_handler.ResponseEntity.*


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
    fun registerError(): ResponseEntity {
        return if (email.isNullOrEmpty()
            || username.isNullOrEmpty()
            || password.isNullOrEmpty()
            || confirmPassword.isNullOrEmpty()
        ) {
            REQUIRED_FIELD()
        } else if (!password.equals(confirmPassword)) {
            PASSWORD_MUST_SAME()
        } else {
            NONE()
        }
    }
}

data class LoginFields(
    var email: String? = null,
    var password: String? = null
) {
    fun loginError(): ResponseEntity {
        return if (email.isNullOrEmpty()
            || password.isNullOrEmpty()
        ) {
            REQUIRED_FIELD()
        } else NONE()
    }
}