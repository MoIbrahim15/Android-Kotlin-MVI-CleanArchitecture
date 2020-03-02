package com.mi.mvi.data.response_handler

interface ResponseHandler {
    fun <T : Any>  handleSuccess(data: T): DataState<T>
    fun <T : Any>  handleError(throwable: Throwable): DataState<T>
}