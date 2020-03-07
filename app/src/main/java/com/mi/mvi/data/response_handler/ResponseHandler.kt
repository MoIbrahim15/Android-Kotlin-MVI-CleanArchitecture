package com.mi.mvi.data.response_handler

interface ResponseHandler {
    fun <T> handleError(throwable: Throwable? = null, businessErrorMsg : String? = null): DataState<T>
    fun isNetworkError(msg : String) : Boolean
}