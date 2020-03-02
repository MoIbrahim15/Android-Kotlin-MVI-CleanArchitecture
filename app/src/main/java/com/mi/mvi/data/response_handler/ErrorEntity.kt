package com.mi.mvi.data.response_handler

sealed class ErrorEntity {

    abstract val originalException: Throwable
    data class Network(override val originalException: Throwable) : ErrorEntity()
    data class NotFound(override val originalException: Throwable) : ErrorEntity()
    data class AccessDenied(override val originalException: Throwable) : ErrorEntity()
    data class ServiceUnavailable(override val originalException: Throwable) : ErrorEntity()
    data class Unknown(override val originalException: Throwable) : ErrorEntity()
}
