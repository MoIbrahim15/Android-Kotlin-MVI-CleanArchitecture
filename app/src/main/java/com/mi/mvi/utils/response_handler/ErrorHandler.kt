package com.mi.mvi.utils.response_handler

import com.mi.mvi.R
import com.mi.mvi.utils.response_handler.ErrorConstants.Companion.INVALID_CREDENTIALS
import com.mi.mvi.utils.response_handler.ErrorConstants.Companion.INVALID_PAGE_NUMBER
import com.mi.mvi.utils.response_handler.ErrorConstants.Companion.USERNAME_EXISTS
import retrofit2.HttpException
import java.io.IOException

class ErrorHandler {
    fun <T> invoke(throwable: Throwable? = null, message: String? = null): DataState<T> {
        message?.let {
            return DataState.ERROR(
                Response(
                    getBusinessErrorRes(it), ResponseView.DIALOG()
                )
            )
        }
        return when (throwable) {
            is IOException,
            is HttpException -> {
                DataState.ERROR(
                    Response(
                        R.string.error_something_went_wrong,
                        ResponseView.DIALOG()
                    )
                )
            }
            else -> DataState.ERROR(
                Response(
                    R.string.error_unknown,
                    ResponseView.DIALOG()
                )
            )
        }
    }

    private fun getBusinessErrorRes(message: String): Int {
        return when (message) {
            INVALID_CREDENTIALS -> R.string.error_login_invalid_credentials
            USERNAME_EXISTS -> R.string.error_username_exists
            INVALID_PAGE_NUMBER -> R.string.invalid_page
            else -> R.string.error_something_went_wrong
        }
    }
}