package com.mi.mvi.data.response_handler

class ErrorConstants {
    companion object{
        const val SUCCESS_CODE = 200
        const val GENERIC_AUTH_ERROR = "Error"

        const val INVALID_CREDENTIALS = "Invalid credentials"
        const val USERNAME_EXISTS = "That username is already in use."
        const val RESPONSE_PASSWORD_UPDATE_SUCCESS = "successfully changed password"

    }
}