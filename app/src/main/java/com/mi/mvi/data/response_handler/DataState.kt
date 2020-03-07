package com.mi.mvi.data.response_handler

import com.mi.mvi.utils.SingleLiveData

sealed class DataState<T>(
    var loading: Loading = Loading(false),
    var data: Data<T>? = null,
    var error: SingleLiveData<Error>? = null
) {
    class LOADING<T>(
        isLoading: Boolean,
        cachedData: T? = null
    ) : DataState<T>(
        loading = Loading(isLoading),
        data = Data(data = SingleLiveData.dataEvent(cachedData))
    )

    class SUCCESS<T>(data: T? = null, response: Response? = null) : DataState<T>(
        data = Data(SingleLiveData.dataEvent(data), SingleLiveData.dataEvent(response))
    )

    class ERROR<T>(response: Response? = null) : DataState<T>(
        error = SingleLiveData.dataEvent(Error(response))
    )
}