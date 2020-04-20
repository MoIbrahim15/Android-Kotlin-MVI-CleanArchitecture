package com.mi.mvi.utils

import androidx.lifecycle.LiveData

// To send null liveData
class AbsentLiveData<T : Any?> private constructor() : LiveData<T>() {

    init {
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}
