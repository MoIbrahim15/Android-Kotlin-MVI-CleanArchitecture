package com.mi.mvi.data.response_handler

import com.mi.mvi.utils.SingleLiveData


sealed class DataState<T>(
    var loading: Boolean = false,
    var data: SingleLiveData<T>? = null,
    var error: SingleLiveData<ErrorResponse>? = null
) {
    class LOADING<T>(
        isLoading: Boolean,
        cachedData: T? = null
    ) : DataState<T>(
        loading = isLoading,
        data = SingleLiveData.dataEvent(cachedData) // data is optional in loading state for caching purpose
    )

    class SUCCESS<T>(data: T? = null) : DataState<T>(
        data = SingleLiveData.dataEvent(data)
    )

    class ERROR<T>(errorResponse: ErrorResponse?) : DataState<T>(
        error = SingleLiveData.dataEvent(errorResponse)
    )
}