package com.mi.mvi.data.repository

import androidx.lifecycle.liveData
import com.mi.mvi.data.network.responses.BaseResponse
import com.mi.mvi.data.response_handler.*
import com.mi.mvi.ui.auth.state.AuthViewState
import com.mi.mvi.utils.Constants.Companion.GENERIC_AUTH_ERROR
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<ResponseObject : BaseResponse, ViewStateType>(
    val responseHandler: ResponseHandler,
    val isNetworkAvailable: Boolean,
    val isNetworkRequest: Boolean
) {

    fun call(): Flow<DataState<AuthViewState>> = flow{
        emit(DataState.LOADING(isLoading = true))

        if (isNetworkRequest) {
            if (isNetworkAvailable) {
                val apiResponse = createNetworkRequest()
                try {
                    apiResponse?.let {
                        if (apiResponse.response != GENERIC_AUTH_ERROR) {
                            handleSuccess(apiResponse)
                        } else {
                            emit(responseHandler.handleError(businessErrorMsg = apiResponse.errorMessage))
                        }
                    }
                } catch (cancelationException: CancellationException) {
                } catch (exception: Exception) {
                    emit(responseHandler.handleError(throwable = exception))
                }
            } else {
                emit(DataState.ERROR(Response(ResponseEntity.Network(), ResponseView.DIALOG())))
            }
        } else {
            createCacheRequest()
        }
    }


    abstract suspend fun createNetworkRequest(): ResponseObject?
    abstract suspend fun createCacheRequest()
    abstract suspend fun handleSuccess(response: ResponseObject)
}