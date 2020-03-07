package com.mi.mvi.utils

class Constants {

    companion object{
        const val BASE_URL = "https://open-api.xyz/api/"

        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"
        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
        const val GENERIC_AUTH_ERROR = "Error"
        const val ERROR_SAVE_AUTH_TOKEN = "Error saving authentication token.\nTry restarting the app."
        const val ERROR_SAVE_ACCOUNT_PROPERTIES = "Error saving account properties.\nTry restarting the app."
        const val ERROR_UNKNOWN = "Unknown error"

        fun isNetworkError(msg: String): Boolean{
            return when{
                msg.contains(UNABLE_TO_RESOLVE_HOST) -> true
                else-> false
            }
        }

    }
}