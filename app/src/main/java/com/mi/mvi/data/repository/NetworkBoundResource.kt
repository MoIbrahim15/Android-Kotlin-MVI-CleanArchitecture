package com.mi.mvi.data.repository

import com.mi.mvi.R
import com.mi.mvi.data.network.responses.BaseResponse
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.response_handler.ErrorConstants.Companion.GENERIC_AUTH_ERROR
import com.mi.mvi.data.response_handler.ErrorHandler
import com.mi.mvi.data.response_handler.Response
import com.mi.mvi.data.response_handler.ResponseView
import com.mi.mvi.ui.auth.state.AuthViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<ResponseObject : BaseResponse, ViewStateType>(
    private val errorHandler: ErrorHandler,
    private val isNetworkAvailable: Boolean,
    private val isNetworkRequest: Boolean
) {

    fun call(): Flow<DataState<AuthViewState>> = flow {
        emit(DataState.LOADING(isLoading = true))

        if (isNetworkRequest) {
            if (isNetworkAvailable) {
                try {
                    val apiResponse = createNetworkRequest()
                    apiResponse?.let {
                        if (apiResponse.response != GENERIC_AUTH_ERROR) {
                            handleSuccess(apiResponse)
                        } else {
                            emit(errorHandler.invoke(message = apiResponse.errorMessage))
                        }
                    }
                } catch (exception: Exception) {
                    emit(errorHandler.invoke(throwable = exception))
                }
            } else {
                emit(
                    DataState.ERROR(
                        Response(
                            R.string.error_no_internet_connection,
                            ResponseView.DIALOG()
                        )
                    )
                )
            }
        } else {
            createCacheRequest()
        }
    }


    abstract suspend fun createNetworkRequest(): ResponseObject?
    abstract suspend fun createCacheRequest()
    abstract suspend fun handleSuccess(response: ResponseObject)
}