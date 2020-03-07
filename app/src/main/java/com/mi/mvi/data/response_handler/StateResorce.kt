package com.mi.mvi.data.response_handler

import com.mi.mvi.utils.SingleLiveData

data class Loading(val isLoading: Boolean)
data class Data<T>(val data: SingleLiveData<T>? = null, val response: SingleLiveData<Response>? = null)
data class Error(val response: Response? = null)


data class Response(
    val responseEntity: ResponseEntity?,
    val responseView: ResponseView?
)

sealed class ResponseView {
    class TOAST() : ResponseView()
    class DIALOG() : ResponseView()
    class NONE() : ResponseView()
}

sealed class ResponseEntity {

    data class Business(val message: String?) : ResponseEntity()
    class Network() : ResponseEntity()
    class NotFound() : ResponseEntity()
    class AccessDenied() : ResponseEntity()
    class ServiceUnavailable() : ResponseEntity()
    class Unknown() : ResponseEntity()
    //adding throw app
    class NONE : ResponseEntity()
    class REQUIRED_FIELD : ResponseEntity()
    class PASSWORD_MUST_SAME : ResponseEntity()
    class CAN_NOT_SAVE : ResponseEntity()

}

