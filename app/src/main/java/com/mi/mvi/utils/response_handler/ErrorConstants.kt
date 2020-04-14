package com.mi.mvi.utils.response_handler

class ErrorConstants {
    companion object {
        const val SUCCESS_CODE = 200
        const val GENERIC_AUTH_ERROR = "Error"

        const val INVALID_CREDENTIALS = "Invalid credentials"
        const val USERNAME_EXISTS = "That username is already in use."
        const val RESPONSE_PASSWORD_UPDATE_SUCCESS = "successfully changed password"
        const val INVALID_PAGE_NUMBER = "Invalid page."
        const val RESPONSE_PERMISSION_TO_EDIT = "You have permission to edit that."
        const val SUCCESS_BLOG_DELETED = "deleted"
        const val RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER = "You must become a member on Codingwithmitch.com to access the API. Visit https://codingwithmitch.com/enroll/"
        const val RESPONSE_CODINGWITHMITCH_MEMBER = "created"
    }
}