package com.mi.mvi.data.repository

import com.mi.mvi.R
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.response_handler.ErrorHandler
import com.mi.mvi.data.response_handler.Response
import com.mi.mvi.data.response_handler.ResponseView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>(
    private val apiCall: (suspend () -> NetworkObj?)?,
    private val cacheCall: (suspend () -> CacheObj?)?,
    private val errorHandler: ErrorHandler,
    private val isNetworkAvailable: Boolean
) {

    fun call(): Flow<DataState<ViewState>> = flow {
        emit(DataState.LOADING(isLoading = true))

        if (cacheCall != null) {
            val cacheResponse = cacheCall.invoke()
            handleCacheSuccess(cacheResponse)
        }
        if (apiCall != null) {
            if (isNetworkAvailable) {
                try {
                    val apiResponse = apiCall.invoke()
                    apiResponse?.let {
                        handleNetworkSuccess(apiResponse)
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
        }

    }

    abstract suspend fun handleCacheSuccess(response: CacheObj?)
    abstract suspend fun handleNetworkSuccess(response: NetworkObj)
}