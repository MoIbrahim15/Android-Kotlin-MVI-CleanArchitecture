package com.mi.mvi.utils

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
class SingleLiveData<T>(private val content: T) {


    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    companion object{

        // we don't want an event if there's no data
        fun <T> dataEvent(data: T?): SingleLiveData<T>?{
            data?.let {
                return SingleLiveData(it)
            }
            return null
        }

        // we don't want an event if there is no message
        fun messageEvent(message: String?): SingleLiveData<String>?{
            message?.let{
                return SingleLiveData(message)
            }
            return null
        }
    }


}