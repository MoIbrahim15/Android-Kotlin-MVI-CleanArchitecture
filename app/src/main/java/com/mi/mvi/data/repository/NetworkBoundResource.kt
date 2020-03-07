package com.mi.mvi.data.repository

import androidx.lifecycle.liveData
import com.mi.mvi.data.network.responses.BaseResponse
import com.mi.mvi.data.response_handler.*
import com.mi.mvi.utils.Constants.Companion.GENERIC_AUTH_ERROR
import kotlinx.coroutines.Dispatchers.IO

abstract class NetworkBoundResource<ResponseObject : BaseResponse, ViewStateType>(
    val responseHandler: ResponseHandler,
    val isNetworkAvailable: Boolean,
    val isNetworkRequest: Boolean
) {

    fun call() = liveData(IO) {
        emit(DataState.LOADING(isLoading = true))

        if (isNetworkRequest) {
            if (isNetworkAvailable) {
                val apiResponse = createNetworkRequest()
                try {
                    apiResponse?.let {
                        if (apiResponse.response != GENERIC_AUTH_ERROR) {
                            emit(handleSuccess(apiResponse))
                        } else {
                            emit(responseHandler.handleError(businessErrorMsg = apiResponse.errorMessage))
                        }
                    }
                } catch (exception: Exception) {
                    emit(responseHandler.handleError(throwable = exception))
                }
            } else {
                emit(DataState.ERROR(Response(ResponseEntity.Network(), ResponseView.DIALOG())))
            }
        } else {
            emit(createCacheRequest())
        }
    }


    abstract suspend fun createNetworkRequest(): ResponseObject?
    abstract suspend fun createCacheRequest() :DataState<ViewStateType>
    abstract fun handleSuccess(response: ResponseObject): DataState<ViewStateType>
}