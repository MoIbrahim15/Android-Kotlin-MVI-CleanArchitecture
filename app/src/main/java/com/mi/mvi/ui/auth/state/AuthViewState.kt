package com.mi.mvi.ui.auth.state

import com.mi.mvi.data.models.AuthToken


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

    class RegistrationError {
        companion object {
            fun mustFillAllFields(): String {
                return "All Fields are required"
            }

            fun passwordsDoNotMatch(): String {
                return "Passwords must match"
            }

            fun none(): String {
                return ""
            }
        }
    }

    fun isValid(): String {
        return if (email.isNullOrEmpty()
            || username.isNullOrEmpty()
            || password.isNullOrEmpty()
            || confirmPassword.isNullOrEmpty()
        ) {
            RegistrationError.mustFillAllFields()
        } else if (!password.equals(confirmPassword)) {
            RegistrationError.passwordsDoNotMatch()
        } else {
            RegistrationError.none()
        }
    }
}


data class LoginFields(
    var email: String? = null,
    var password: String? = null
) {
    class LoginError {
        companion object {
            fun mustFillAllFields(): String {
                return "You can't login without an email and password."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun isValid(): String {
        return if (email.isNullOrEmpty()
            || password.isNullOrEmpty()
        ) {
            LoginError.mustFillAllFields()
        } else LoginError.none()
    }
}