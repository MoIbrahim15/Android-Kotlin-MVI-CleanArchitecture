package com.mi.mvi.data.response_handler

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection.*

class ResponseHandlerImpl :
    ResponseHandler {

    override fun <T : Any> handleSuccess(data: T): DataState<T> {
        return DataState.SUCCESS(data)
    }

    override fun <T : Any> handleError(throwable: Throwable): DataState<T> {
        return when (throwable) {
            is IOException ->
                DataState.ERROR(
                    ErrorResponse(
                        ErrorEntity.Network(
                            throwable
                        ), ErrorState.DIALOG()
                    )
                )
            is HttpException -> {
                when (throwable.code()) {
                    HTTP_GATEWAY_TIMEOUT -> DataState.ERROR(
                        ErrorResponse(
                            ErrorEntity.Network(throwable),
                            ErrorState.DIALOG()
                        )
                    )
                    HTTP_NOT_FOUND -> DataState.ERROR(
                        ErrorResponse(
                            ErrorEntity.NotFound(throwable),
                            ErrorState.DIALOG()
                        )
                    )
                    HTTP_FORBIDDEN -> DataState.ERROR(
                        ErrorResponse(
                            ErrorEntity.AccessDenied(throwable),
                            ErrorState.DIALOG()
                        )
                    )
                    HTTP_UNAVAILABLE -> DataState.ERROR(
                        ErrorResponse(
                            ErrorEntity.ServiceUnavailable(throwable),
                            ErrorState.DIALOG()
                        )
                    )
                    else -> DataState.ERROR(
                        ErrorResponse(
                            ErrorEntity.Unknown(throwable),
                            ErrorState.DIALOG()
                        )
                    )
                }
            }
            else -> DataState.ERROR(
                ErrorResponse(
                    ErrorEntity.Unknown(throwable),
                    ErrorState.DIALOG()
                )
            )
        }
    }
}