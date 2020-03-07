package com.mi.mvi.data.response_handler

import com.mi.mvi.utils.Constants.Companion.UNABLE_TO_RESOLVE_HOST
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection.*

class ResponseHandlerImpl :
    ResponseHandler {

    override fun <T> handleError(throwable: Throwable?, businessErrorMsg: String?): DataState<T> {
        businessErrorMsg?.let{ message ->
            return DataState.ERROR(
                Response(
                    ResponseEntity.Business(message), ResponseView.DIALOG()
                )
            )
        }
        return when (throwable) {
            is IOException ->
                DataState.ERROR(
                    Response(
                        ResponseEntity.Network(), ResponseView.DIALOG()
                    )
                )
            is HttpException -> {
                when (throwable.code()) {
                    HTTP_GATEWAY_TIMEOUT -> DataState.ERROR(
                        Response(
                            ResponseEntity.Network(),
                            ResponseView.DIALOG()
                        )
                    )
                    HTTP_NOT_FOUND -> DataState.ERROR(
                        Response(
                            ResponseEntity.NotFound(),
                            ResponseView.DIALOG()
                        )
                    )
                    HTTP_FORBIDDEN -> DataState.ERROR(
                        Response(
                            ResponseEntity.AccessDenied(),
                            ResponseView.DIALOG()
                        )
                    )
                    HTTP_UNAVAILABLE -> DataState.ERROR(
                        Response(
                            ResponseEntity.ServiceUnavailable(),
                            ResponseView.DIALOG()
                        )
                    )
                    else -> DataState.ERROR(
                        Response(
                            ResponseEntity.Unknown(),
                            ResponseView.DIALOG()
                        )
                    )
                }
            }
            else -> DataState.ERROR(
                Response(
                    ResponseEntity.Unknown(),
                    ResponseView.DIALOG()
                )
            )
        }
    }

    override fun isNetworkError(msg: String): Boolean {
        return when {
            msg.contains(UNABLE_TO_RESOLVE_HOST) -> true
            else -> false
        }
    }
}