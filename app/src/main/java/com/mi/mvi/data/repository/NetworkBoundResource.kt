package com.mi.mvi.data.repository

import com.mi.mvi.utils.Constants.Companion.CACHE_ERROR_TIMEOUT
import com.mi.mvi.utils.Constants.Companion.CACHE_TIMEOUT
import com.mi.mvi.utils.Constants.Companion.NETWORK_ERROR_TIMEOUT
import com.mi.mvi.utils.Constants.Companion.NETWORK_TIMEOUT
import com.mi.mvi.utils.Constants.Companion.UNKNOWN_ERROR
import com.mi.mvi.utils.response_handler.DataState
import com.mi.mvi.utils.response_handler.MessageType
import com.mi.mvi.utils.response_handler.StateMessage
import com.mi.mvi.utils.response_handler.UIComponentType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import com.mi.mvi.utils.Constants.Companion.NETWORK_ERROR as NETWORK_ERROR1

@ExperimentalCoroutinesApi
abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>(
    private val dispatcher: CoroutineDispatcher,
    private val apiCall: (suspend () -> NetworkObj?)? = null,
    val cacheCall: (suspend () -> CacheObj?)? = null
) {

    val result: Flow<DataState<ViewState>> = flow {
        emit(DataState.LOADING(isLoading = true))
        cacheCall?.let {
            emitAll(safeCacheCall())
        }

        apiCall?.let {
            emitAll(safeAPICall())
        }
    }

    private suspend fun safeAPICall() = flow<DataState<ViewState>> {
        withContext(dispatcher) {
            try {
                // throws TimeoutCancellationException
                withTimeout(NETWORK_TIMEOUT) {
                    val result = apiCall?.invoke()
                    if (result == null) {
                        emit(buildDialogError(UNKNOWN_ERROR))
                    } else {
                        updateCache(result)
                        handleNetworkSuccess(result)?.let {
                            emit(it)
                        }
                    }
                }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                when (throwable) {
                    is TimeoutCancellationException -> {
                        emit(buildDialogError(NETWORK_ERROR_TIMEOUT))
                    }
                    is IOException -> {
                        emit(buildDialogError(NETWORK_ERROR1))
                    }
                    is HttpException -> {
                        emit(buildDialogError(convertErrorBody(throwable)))
                    }
                    else -> {
                        emit(buildDialogError(UNKNOWN_ERROR))
                    }
                }
            }
        }
    }


    private suspend fun safeCacheCall() = flow<DataState<ViewState>> {
        withContext(dispatcher) {
            try {
                // throws TimeoutCancellationException
                withTimeout(CACHE_TIMEOUT) {
                    val result = cacheCall?.invoke()
                    handleCacheSuccess(result)?.let {
                        emit(it)
                    }
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is TimeoutCancellationException -> {
                        emit(buildDialogError(CACHE_ERROR_TIMEOUT))
                    }
                    else -> {
                        emit(buildDialogError(UNKNOWN_ERROR))
                    }
                }
            }
        }
    }


    fun buildDialogError(
        message: String?
    ): DataState<ViewState> {
        return DataState.ERROR(
            StateMessage(
                message = message,
                uiComponentType = UIComponentType.DIALOG,
                messageType = MessageType.ERROR
            )
        )
    }


    private fun convertErrorBody(throwable: HttpException): String {
        return try {
            throwable.response()?.errorBody()?.string() ?: UNKNOWN_ERROR
        } catch (exception: Exception) {
            UNKNOWN_ERROR
        }
    }

    open suspend fun handleNetworkSuccess(response: NetworkObj)
            : DataState<ViewState>? {
        return null
    }

    open suspend fun handleCacheSuccess(response: CacheObj?)
            : DataState<ViewState>? {
        return null
    }

    open suspend fun updateCache(networkObject: NetworkObj) {}

}