package com.mi.mvi.presentation.auth.state

import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.utils.ErrorHandling.Companion.ERROR_ALL_FIELDS_ARE_REQUIRED
import com.mi.mvi.utils.ErrorHandling.Companion.ERROR_PASSWORD_DOESNOT_MATCH
import com.mi.mvi.utils.SuccessHandling.Companion.SUCCESS


data class AuthViewState(
    var registrationFields: RegistrationFields? = null,
    var loginFields: LoginFields? = null,
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
                return ERROR_ALL_FIELDS_ARE_REQUIRED
            }

            fun passwordsDoNotMatch(): String {
                return ERROR_PASSWORD_DOESNOT_MATCH
            }

            fun none(): String {
                return SUCCESS
            }

        }
    }

    fun isValidForRegistration(): String {
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
                return ERROR_ALL_FIELDS_ARE_REQUIRED
            }

            fun none(): String {
                return SUCCESS
            }
        }
    }

    fun isValidForLogin(): String {
        return if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            LoginError.mustFillAllFields()
        } else LoginError.none()
    }
}