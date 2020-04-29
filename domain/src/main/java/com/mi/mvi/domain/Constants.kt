package com.mi.mvi.domain

class Constants {
    companion object {

        // Shared Preference Keys


        // -----------------------------NETWORK-----------------------
        const val PASSWORD_RESET_URL: String = "https://open-api.xyz/password_reset/"
        const val NETWORK_TIMEOUT = 6000L
        const val CACHE_TIMEOUT = 2000L


        // -----------------------------SUCCESS-----------------------
        const val SUCCESS = "success"
        const val DELETE = "deleted"
        const val RESPONSE_PASSWORD_UPDATE_SUCCESS = "successfully changed password"
        const val RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE =
            "Done checking for previously authenticated user."
        const val RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER =
            "You must become a member on Codingwithmitch.com to access the API. Visit https://codingwithmitch.com/enroll/"
        const val RESPONSE_NO_PERMISSION_TO_EDIT = "You don't have permission to edit that."
        const val RESPONSE_HAS_PERMISSION_TO_EDIT = "You have permission to edit that."
        const val SUCCESS_BLOG_CREATED = "created"
        const val SUCCESS_BLOG_DELETED = "deleted"
        const val SUCCESS_BLOG_UPDATED = "updated"

        // -----------------------------ERROR-----------------------
        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val UNABLE_TODO_OPERATION_WO_INTERNET =
            "Can't do that operation without an internet connection"

        const val ERROR_SAVE_ACCOUNT_PROPERTIES =
            "Error saving account properties.\nTry restarting the app."
        const val ERROR_SAVE_AUTH_TOKEN =
            "Error saving authentication token.\nTry restarting the app."
        const val ERROR_SOMETHING_WRONG_WITH_IMAGE = "Something went wrong with the image."
        const val ERROR_MUST_SELECT_IMAGE = "You must select an image."

        const val GENERIC_AUTH_ERROR = "Error"
        const val INVALID_PAGE = "Invalid page."
        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
        const val ERROR_UNKNOWN = "Unknown error"
        const val INVALID_CREDENTIALS = "Invalid credentials"
        const val SOMETHING_WRONG_WITH_IMAGE = "Something went wrong with the image."
        const val INVALID_STATE_EVENT = "Invalid state event"
        const val CANNOT_BE_UNDONE = "This can't be undone."
        const val NETWORK_ERROR = "Network error"
        const val NETWORK_ERROR_TIMEOUT = "Network timeout"
        const val CACHE_ERROR_TIMEOUT = "Cache timeout"
        const val UNKNOWN_ERROR = "Unknown error"
        const val ERROR_ALL_FIELDS_ARE_REQUIRED = "All fields are required"
        const val ERROR_PASSWORD_DOESNOT_MATCH = "Password and confirm password must be same"
        const val INVALID_PAGE_NUMBER = "invalid page number"



    }
}