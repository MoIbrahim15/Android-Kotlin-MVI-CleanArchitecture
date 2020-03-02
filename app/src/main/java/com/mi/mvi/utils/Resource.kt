package com.mi.mvi.utils


sealed class Resource<T>(
    var loading: Boolean = false,
    var data: SingleLiveData<T>? = null,
    var message: SingleLiveData<String>? = null
) {
    class LOADING<T>(
        isLoading: Boolean,
        data: T? = null
    ) : Resource<T>(
        loading = isLoading,
        data = null
    )

    class SUCCESS<T>(
        data: T? = null,
        isLoading: Boolean = false
    ) : Resource<T>(
        loading = isLoading,
        data = SingleLiveData.dataEvent(data)

    )

    class ERROR<T>(
        message: String
    ) : Resource<T>(message = SingleLiveData(message))
}