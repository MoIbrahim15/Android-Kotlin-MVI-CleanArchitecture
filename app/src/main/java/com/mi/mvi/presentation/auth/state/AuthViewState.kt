package com.mi.mvi.presentation.auth.state

import com.mi.mvi.R
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.utils.response_handler.ErrorConstants.Companion.SUCCESS_CODE


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
        companion object{

            fun mustFillAllFields(): Int{
                return  R.string.error_all_fields_are_required
            }

            fun passwordsDoNotMatch(): Int{
                return R.string.error_passwords_must_same
            }

            fun none():Int{
                return SUCCESS_CODE
            }

        }
    }
    fun isValidForRegistration(): Int {
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
            fun mustFillAllFields(): Int {
                return R.string.error_all_fields_are_required
            }

            fun none(): Int {
                return SUCCESS_CODE
            }
        }
    }

    fun isValidForLogin(): Int {
        return if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            LoginError.mustFillAllFields()
        } else LoginError.none()
    }
}