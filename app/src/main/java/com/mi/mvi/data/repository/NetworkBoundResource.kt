package com.mi.mvi.data.repository

import com.mi.mvi.R
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.ErrorHandler
import com.mi.mvi.utils.response_handler.Response
import com.mi.mvi.utils.response_handler.ResponseView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>(
    private val apiCall: (suspend () -> NetworkObj?)?,
    val cacheCall: (suspend () -> CacheObj?)?,
    private val errorHandler: ErrorHandler,
    private val isNetworkAvailable: Boolean,
    private val canWorksOffline: Boolean
) {

    fun call(): Flow<DataState<ViewState>> = flow {
        emit(DataState.LOADING(isLoading = true))

        if (cacheCall != null) {
            val cacheResponse = cacheCall?.invoke()
            handleCacheSuccess(cacheResponse)
        }
        if (apiCall != null) {
            if (isNetworkAvailable) {
                emitAll(doNetworkRequest())
            } else {
                if (!canWorksOffline)
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

    private fun doNetworkRequest() = flow<DataState<ViewState>> {
        try {
            val apiResponse = apiCall?.invoke()
            apiResponse?.let {
                handleNetworkSuccess(apiResponse)
            }
        } catch (exception: Exception) {
            emit(errorHandler.invoke(throwable = exception))
        }
    }

    abstract suspend fun handleNetworkSuccess(response: NetworkObj)
    abstract suspend fun handleCacheSuccess(response: CacheObj?)
}